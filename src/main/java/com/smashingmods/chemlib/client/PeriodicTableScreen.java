package com.smashingmods.chemlib.client;

import com.smashingmods.chemlib.ChemLib;
import com.smashingmods.chemlib.api.Element;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;

public class PeriodicTableScreen extends Screen {

    public PeriodicTableScreen() {
        super(Component.literal("Periodic Table"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor matrices, int mouseX, int mouseY, float delta) {

        int imageWidth = 2000;
        int imageHeight = 1016;
        int displayWidth = imageWidth / 4;
        int displayHeight = imageHeight / 4;
        int leftPos = (this.width - displayWidth) / 2;
        int topPos = (this.height - displayHeight) / 2;

        matrices.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, "textures/gui/periodic_table.png"),
                leftPos, topPos, 0, 0, displayWidth, displayHeight, imageWidth, imageHeight, imageWidth, imageHeight);
        matrices.centeredText(this.font, Component.translatable("chemlib.screen.periodic_table").withStyle(ChatFormatting.BOLD), width / 2, 24, 0xFFFFFFFF);

        double boxWidth = 27.75f;
        double boxHeight = 26.9f;
        double startX = (this.width - (18 * boxWidth)) / 2;
        double startY = ((this.height - (7 * boxHeight)) / 2) - 33.0f;
        int count = 0;

        for (Element element : ItemRegistry.getElements()) {
            double x = startX;
            double y = startY;
            int group = element.getElementGroup();
            int period = element.getPeriod();

            for (int row = 1; row <= 7; row++) {
                if (row == period) {
                    for (int col = 1; col <= 18; col++) {
                        if (col == group) {
                            if (!((period == 6 || period == 7) && group == 3)) {
                                if (mouseX >= x && mouseX < x + boxWidth && mouseY >= y && mouseY < y + boxHeight) {
                                    drawElementTip(matrices, element);
                                }
                            } else {
                                double resetX = x;
                                double resetY = y;
                                if (period == 6) {
                                    y = (boxHeight * 7.45f) + startY;
                                    x = (boxWidth * count) + startX + boxWidth * 2;
                                    if (mouseX >= x && mouseX < x + boxWidth && mouseY >= y && mouseY < y + boxHeight) {
                                        drawElementTip(matrices, element);
                                    }
                                    count++;
                                }
                                if (period == 7) {
                                    y = (boxHeight * 8.45f) + startY;
                                    x = (boxWidth * (count - 15)) + startX + boxWidth * 2;
                                    if (mouseX >= x && mouseX < x + boxWidth && mouseY >= y && mouseY < y + boxHeight) {
                                        drawElementTip(matrices, element);
                                    }
                                    count++;
                                }
                                x = resetX;
                                y = resetY;
                            }
                        }
                        x += boxWidth;
                    }
                }
                x = startX;
                y += boxHeight;
            }
        }
        super.extractRenderState(matrices, mouseX, mouseY, delta);
    }

    private void drawElementTip(GuiGraphicsExtractor matrices, Element pElement) {
        matrices.blit(RenderPipelines.GUI_TEXTURED,
                Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, String.format("textures/gui/elements/%s_tooltip.png", pElement.getChemicalName())),
                ((this.width - 276) / 2) - 55, ((this.height - (7 * 28)) / 2) - 30, 0, 0, 274, 80, 40, 40, 40, 40);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
