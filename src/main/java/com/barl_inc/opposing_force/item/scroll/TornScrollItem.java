package com.barl_inc.opposing_force.item.scroll;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFDataComponents;
import com.barl_inc.opposing_force.registry.OFTornScrolls;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.world.SinewWorldData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TornScrollItem extends Item {

    public TornScrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.has(OFDataComponents.TORN_SCROLL_VARIANT.get())) {
            if (level.isClientSide) {
                OpposingForce.PROXY.openTornScroll(stack);
            }
        } else if (level instanceof ServerLevel serverLevel) {
            read(serverLevel, player, stack);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void read(ServerLevel level, Player player, ItemStack stack) {
        roll(level, player, stack.getOrDefault(OFDataComponents.TORN_SCROLL_ORIGIN.get(), TornScrollOrigin.NONE)).ifPresent(variant -> {
            stack.set(OFDataComponents.TORN_SCROLL_VARIANT.get(), variant);
            stack.remove(OFDataComponents.TORN_SCROLL_ORIGIN.get());
            List<ResourceLocation> received = player.getData(OFTornScrolls.RECEIVED_SCROLLS.get());
            ResourceLocation id = variant.unwrapKey().map(ResourceKey::location).orElse(OpposingForce.location("blank"));
            if (!received.contains(id)) {
                List<ResourceLocation> updated = new ArrayList<>(received);
                updated.add(id);
                player.setData(OFTornScrolls.RECEIVED_SCROLLS.get(), updated);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        });
    }

    private static Optional<Holder<TornScrollVariant>> roll(ServerLevel level, Player player, TornScrollOrigin origin) {
        Registry<TornScrollVariant> registry = level.registryAccess().registryOrThrow(OFTornScrolls.TORN_SCROLL);
        Optional<Holder<Structure>> structure = origin.structure().flatMap(key -> level.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolder(key)).map(holder -> holder);
        Optional<Holder<Biome>> biome = origin.biome().flatMap(key -> level.registryAccess().registryOrThrow(Registries.BIOME).getHolder(key)).map(holder -> holder);
        TornScrollVariant.Stage stage = getWorldStage(level);
        List<Holder.Reference<TornScrollVariant>> eligible = registry.holders().filter(variant -> matches(variant.value().biomes(), biome)).toList();
        List<Holder.Reference<TornScrollVariant>> structured = eligible.stream().filter(variant -> variant.value().structures().isPresent() && matches(variant.value().structures(), structure)).toList();
        List<Holder.Reference<TornScrollVariant>> pool = structured.isEmpty() ? eligible.stream().filter(variant -> variant.value().structures().isEmpty() && variant.value().stage().ordinal() <= stage.ordinal()).toList() : structured;
        List<ResourceLocation> received = player.getData(OFTornScrolls.RECEIVED_SCROLLS.get());
        SimpleWeightedRandomList.Builder<Holder<TornScrollVariant>> weights = SimpleWeightedRandomList.builder();
        pool.stream().filter(variant -> variant.value().weight() > 0).forEach(variant -> weights.add(variant, variant.value().weight() * (received.contains(variant.key().location()) ? 1 : 3)));
        return weights.build().getRandomValue(level.getRandom()).or(() -> registry.getHolder(ResourceKey.create(OFTornScrolls.TORN_SCROLL, OpposingForce.location("blank"))).map(holder -> holder));
    }

    private static <T> boolean matches(Optional<HolderSet<T>> set, Optional<Holder<T>> holder) {
        return set.map(values -> holder.map(values::contains).orElse(false)).orElse(true);
    }

    private static TornScrollVariant.Stage getWorldStage(ServerLevel level) {
        SinewWorldData data = SinewWorldData.get(level);
        TornScrollVariant.Stage stage = TornScrollVariant.Stage.PRE_NETHER;
        if (!SinewConfig.ENABLE_PROGRESSION.get() || data.hasDragonBeenKilledBefore()) {
            stage = TornScrollVariant.Stage.POST_END;
        } else if (data.hasNetherBeenEnteredBefore()) {
            stage = TornScrollVariant.Stage.POST_NETHER;
        }
        return stage;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Holder<TornScrollVariant> variant = stack.get(OFDataComponents.TORN_SCROLL_VARIANT.get());
        if (variant != null) {
            tooltipComponents.add(variant.value().name().copy().withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(variant.value().stage().getDisplayName());
        } else {
            tooltipComponents.add(Component.translatable("torn_scroll.opposing_force.unread").withStyle(ChatFormatting.GRAY));
        }
    }

    public static void addCreativeVariants(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, Item item) {
        output.accept(new ItemStack(item));
        parameters.holders().lookup(OFTornScrolls.TORN_SCROLL).ifPresent(lookup -> lookup.listElements()
                .sorted(Comparator.comparing((Holder.Reference<TornScrollVariant> variant) -> variant.value().stage().ordinal()).thenComparing(variant -> variant.key().location().getPath()))
                .forEach(variant -> {
                    ItemStack stack = new ItemStack(item);
                    stack.set(OFDataComponents.TORN_SCROLL_VARIANT.get(), variant);
                    output.accept(stack);
                }));
    }
}