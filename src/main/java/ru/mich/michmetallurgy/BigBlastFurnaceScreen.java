package ru.mich.michmetallurgy;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import ru.mich.michmetallurgy.MichMetallurgy;
import ru.mich.michmetallurgy.BigBlastFurnaceMenu;

public class BigBlastFurnaceScreen extends AbstractContainerScreen<BigBlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/big_blast_furnace.png");

    public BigBlastFurnaceScreen(BigBlastFurnaceMenu menu, Inventory inventory, Component title) {
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

        // Base GUI background
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Lit Flame Animation (x25 y58, size 13x13, UV x177 y1)
        if (this.menu.isLit()) {
            int flameHeight = this.menu.getLitProgress(13);
            guiGraphics.blit(TEXTURE,
                    x + 24, y + 56 + (14 - flameHeight),
                    177, 0 + (14 - flameHeight),
                    14, flameHeight);
        }

        // Progress Arrow Animation (x79 y39, size 22x15, UV x177 y14)
        int arrowWidth = this.menu.getBurnProgress(23);
        guiGraphics.blit(TEXTURE,
                x + 78, y + 37,
                177, 14,
                arrowWidth, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}