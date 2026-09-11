package com.smashingmods.chemlib.test;

import com.smashingmods.chemlib.client.PeriodicTableScreen;
import com.smashingmods.chemlib.registry.ItemRegistry;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.MissingItemModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChemLibClientTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        context.getInput().resizeWindow(1200, 800);
        try (var world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            context.runOnClient(client -> { client.options.guiScale().set(2); client.resizeGui(); });
            context.runOnClient(client -> {
                List<Item> items = new ArrayList<>(ItemRegistry.getItems());
                items.add(ItemRegistry.PERIODIC_TABLE_ITEM);
                items.addAll(FluidRegistry.BUCKETS);
                for (Item item : items) {
                    var id = BuiltInRegistries.ITEM.getKey(item);
                    if (client.getModelManager().getItemModel(id) instanceof MissingItemModel) {
                        throw new AssertionError("Missing item model: " + id);
                    }
                    for (var mode : ItemDisplayContext.values()) {
                        var state = new ItemStackRenderState();
                        client.getItemModelResolver().updateForTopItem(state, new ItemStack(item), mode, null, null, 0);
                        if (state.isEmpty()) throw new AssertionError("Empty item model: " + id + " / " + mode);
                        state.getModelBoundingBox();
                    }
                }
                for (var fluid : FluidRegistry.FLUIDS) {
                    var model = client.getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());
                    if (model == null || model.tintSource().color(fluid.defaultFluidState().createLegacyBlock()) != fluid.getColor()) {
                        throw new AssertionError("Missing or incorrectly tinted fluid: " + fluid);
                    }
                }
            });
            context.setScreen(Gallery::new);
            context.waitTicks(5);
            context.takeScreenshot("chemlib-items");
            context.runOnClient(client -> {
                client.player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.PERIODIC_TABLE_ITEM));
                ItemRegistry.PERIODIC_TABLE_ITEM.use(client.level, client.player, net.minecraft.world.InteractionHand.MAIN_HAND);
                if (!(client.gui.screen() instanceof PeriodicTableScreen)) throw new AssertionError("Using the periodic table must open its screen");
            });
            context.getInput().setCursorPos(0, 0);
            context.waitTicks(5);
            context.takeScreenshot("chemlib-periodic-table");
            var hover = context.computeOnClient(client -> new double[]{
                    ((client.gui.screen().width - 18 * 27.75) / 2 + 10) * client.getWindow().getGuiScale(),
                    ((client.gui.screen().height - 7 * 26.9) / 2 - 33 + 10) * client.getWindow().getGuiScale()});
            context.getInput().setCursorPos(hover[0], hover[1]);
            context.waitTicks(5);
            context.takeScreenshot("chemlib-hydrogen-tooltip");
            context.setScreen(() -> null);
        }
    }

    private static class Gallery extends Screen {
        Gallery() { super(Component.literal("ChemLib compatibility gallery")); }

        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            graphics.centeredText(font, title, width / 2, 10, -1);
            int index = 0;
            List<Item> items = new ArrayList<>(ItemRegistry.ELEMENTS);
            items.addAll(ItemRegistry.COMPOUNDS.subList(0, 8));
            items.addAll(ItemRegistry.INGOTS.subList(0, 8));
            items.addAll(ItemRegistry.CHEMICAL_BLOCK_ITEMS.subList(0, 8));
            items.addAll(FluidRegistry.BUCKETS.subList(0, 8));
            for (Item item : items) {
                int x = 12 + (index % 20) * 22;
                int y = 30 + (index / 20) * 24;
                graphics.item(new ItemStack(item), x, y);
                index++;
            }
        }
    }
}
