package com.barl_inc.opposing_force.item.ranged.ammo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record BulletComponent(ItemStack renderItem, float damageMultiplier, float inaccuracyMultiplier, float velocityMultiplier) {

    public static Codec<BulletComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("render_item").forGetter(BulletComponent::renderItem),
            Codec.FLOAT.fieldOf("damage_multiplier").forGetter(BulletComponent::damageMultiplier),
            Codec.FLOAT.fieldOf("inaccuracy_multiplier").forGetter(BulletComponent::inaccuracyMultiplier),
            Codec.FLOAT.fieldOf("velocity_multiplier").forGetter(BulletComponent::velocityMultiplier)
    ).apply(instance, BulletComponent::new));

    public static StreamCodec<RegistryFriendlyByteBuf, BulletComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, bulletComponent -> bulletComponent.renderItem,
            ByteBufCodecs.FLOAT, bulletComponent -> bulletComponent.damageMultiplier,
            ByteBufCodecs.FLOAT, bulletComponent -> bulletComponent.inaccuracyMultiplier,
            ByteBufCodecs.FLOAT, bulletComponent -> bulletComponent.velocityMultiplier,
            BulletComponent::new
    );
}