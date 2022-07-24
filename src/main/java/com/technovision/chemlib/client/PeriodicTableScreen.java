package com.technovision.chemlib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.technovision.chemlib.ChemLib;
import com.technovision.chemlib.api.Element;
import com.technovision.chemlib.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class PeriodicTableScreen extends Screen {

    public PeriodicTableScreen() {
        super(Text.literal("Periodic Table"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int imageWidth = 2000;
        int imageHeight = 1016;
        int displayWidth = imageWidth / 4;
        int displayHeight = imageHeight / 4;
        int leftPos = (this.width - displayWidth) / 2;
        int topPos = (this.height - displayHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, new Identifier(ChemLib.MOD_ID, "textures/gui/periodic_table.png"));
        drawTexture(matrices, leftPos, topPos, displayWidth, displayHeight, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, Text.translatable("chemlib.screen.periodic_table").setStyle(Style.EMPTY.withColor(Formatting.BOLD)), width / 2, 24, 0xFFFFFF);

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
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void drawElementTip(MatrixStack matrices, Element pElement) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, new Identifier(ChemLib.MOD_ID, String.format("textures/gui/elements/%s_tooltip.png", pElement.getChemicalName())));
        drawTexture(matrices, ((this.width - 276) / 2) - 55, ((this.height - (7 * 28)) / 2) - 30, 274, 80, 0, 0, 40, 40, 40, 40);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
