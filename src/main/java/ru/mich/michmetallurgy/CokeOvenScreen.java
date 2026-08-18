package ru.mich.michmetallurgy;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CokeOvenScreen extends AbstractContainerScreen<CokeOvenMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/coke_oven.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/coke_oven_progress.png");
    private static final ResourceLocation FLAME_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/coke_oven_lit.png");

    // Подгони под реальный размер своих спрайтов
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;
    private static final int FLAME_WIDTH = 14;
    private static final int FLAME_HEIGHT = 14;

    public CokeOvenScreen(CokeOvenMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int flameHeight = this.menu.getProgress(FLAME_HEIGHT);
        if (flameHeight > 0) {
            guiGraphics.blit(FLAME_TEXTURE,
                    x + 34, y + 54 + (FLAME_HEIGHT - flameHeight),
                    0, FLAME_HEIGHT - flameHeight,
                    FLAME_WIDTH, flameHeight,
                    FLAME_WIDTH, FLAME_HEIGHT);
        }

        int arrowWidth = this.menu.getProgress(ARROW_WIDTH);
        if (arrowWidth > 0) {
            guiGraphics.blit(ARROW_TEXTURE, x + 65, y + 36, 0, 0, arrowWidth, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}