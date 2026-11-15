package com.barl_inc.opposing_force.client.screen;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.scroll.TornScrollVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TornScrollScreen extends Screen {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/gui/torn_scroll.png");

    private final Component text;
    private List<FormattedCharSequence> lines = List.of();

    public TornScrollScreen(@Nullable Holder<TornScrollVariant> variant) {
        super(variant != null ? variant.value().name() : Component.translatable("torn_scroll.opposing_force.illegible"));
        this.text = variant != null ? variant.unwrapKey().map(TornScrollScreen::loadText).orElse(CommonComponents.EMPTY) : Component.translatable("torn_scroll.opposing_force.illegible");
    }

    private static Component loadText(ResourceKey<TornScrollVariant> key) {
        String language = Minecraft.getInstance().getLanguageManager().getSelected();
        ResourceLocation id = key.location();
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(id.withPath(path -> "texts/torn_scroll/" + language + "/" + path + ".txt"))
                .or(() -> Minecraft.getInstance().getResourceManager().getResource(id.withPath(path -> "texts/torn_scroll/en_us/" + path + ".txt")));
        return resource.map(TornScrollScreen::readText).orElse(CommonComponents.EMPTY);
    }

    private static Component readText(Resource resource) {
        Component text = CommonComponents.EMPTY;
        try (BufferedReader reader = resource.openAsReader()) {
            text = Component.literal(reader.lines().collect(Collectors.joining("\n")));
        } catch (IOException exception) {
            OpposingForce.LOGGER.error("Failed to read torn scroll text", exception);
        }
        return text;
    }

    @Override
    protected void init() {
        super.init();
        List<FormattedCharSequence> split = this.font.split(this.text, 82);
        this.lines = split.subList(0, Math.min(split.size(), 13));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int left = (this.width - 144) / 2;
        int top = (this.height - 177) / 2;
        guiGraphics.blit(TEXTURE, left, top, 0.0F, 0.0F, 144, 177, 144, 177);
        for (int i = 0; i < this.lines.size(); i++) {
            guiGraphics.drawString(this.font, this.lines.get(i), left + 31, top + 34 + i * 9, 0x000000, false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean handled = super.keyPressed(keyCode, scanCode, modifiers);
        if (!handled && this.minecraft != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            handled = true;
        }
        return handled;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}