package ru.mich.michmetallurgy;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PuddlingFurnaceScreen extends AbstractContainerScreen<PuddlingFurnaceMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/puddling_furnace.png");
    private static final ResourceLocation MARKER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/puddling_furnace_marker.png");
    private static final ResourceLocation FLAME_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/puddling_furnace_lit.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/puddling_furnace_progress.png");
    private static final ResourceLocation BATH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "textures/gui/container/puddling_furnace_molten.png");

    // Подгони под реальный размер спрайта пометки
    private static final int MARKER_WIDTH = 2;
    private static final int MARKER_HEIGHT = 3;
    private static final int MARKER_X = 9;
    private static final int MARKER_Y_HOT = 12;
    private static final int MARKER_Y_COLD = 57;

    private static final int TEMP_AREA_X1 = 13, TEMP_AREA_Y1 = 13;
    private static final int TEMP_AREA_X2 = 21, TEMP_AREA_Y2 = 60;

    private static final int FLAME_WIDTH = 14;
    private static final int FLAME_HEIGHT = 14;

    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;

    private static final int BATH_X1 = 51, BATH_Y1 = 28;
    private static final int BATH_X2 = 108, BATH_Y2 = 45;
    private static final int BATH_WIDTH = BATH_X2 - BATH_X1;
    private static final int BATH_HEIGHT = BATH_Y2 - BATH_Y1;

    public PuddlingFurnaceScreen(PuddlingFurnaceMenu menu, Inventory inventory, Component title) {
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

        int bathFill = this.menu.getBathLevel(BATH_HEIGHT);
        if (bathFill > 0) {
            guiGraphics.blit(BATH_TEXTURE,
                    x + BATH_X1, y + BATH_Y2 - bathFill,
                    0, BATH_HEIGHT - bathFill,
                    BATH_WIDTH, bathFill,
                    BATH_WIDTH, BATH_HEIGHT);
        }

        int flameHeight = this.menu.getFlameHeight(FLAME_HEIGHT);
        if (flameHeight > 0) {
            guiGraphics.blit(FLAME_TEXTURE,
                    x + 73, y + 48 + (FLAME_HEIGHT - flameHeight),
                    0, FLAME_HEIGHT - flameHeight,
                    FLAME_WIDTH, flameHeight,
                    FLAME_WIDTH, FLAME_HEIGHT);
        }

        int arrowWidth = this.menu.getArrowWidth(ARROW_WIDTH);
        if (arrowWidth > 0) {
            guiGraphics.blit(ARROW_TEXTURE, x + 118, y + 33, 0, 0, arrowWidth, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
        }

        int markerY = getMarkerY(this.menu.getTemperature());
        guiGraphics.blit(MARKER_TEXTURE, x + MARKER_X, y + markerY, 0, 0, MARKER_WIDTH, MARKER_HEIGHT);
    }

    private int getMarkerY(int temperature) {
        int clamped = Math.max(20, Math.min(1500, temperature));
        float ratio = (clamped - 20) / 1480.0f;
        return Math.round(MARKER_Y_COLD - ratio * (MARKER_Y_COLD - MARKER_Y_HOT));
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int relX = mouseX - x;
        int relY = mouseY - y;

        if (relX >= TEMP_AREA_X1 && relX <= TEMP_AREA_X2 && relY >= TEMP_AREA_Y1 && relY <= TEMP_AREA_Y2) {
            guiGraphics.renderTooltip(this.font, Component.literal(this.menu.getTemperature() + "°C"), mouseX, mouseY);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}