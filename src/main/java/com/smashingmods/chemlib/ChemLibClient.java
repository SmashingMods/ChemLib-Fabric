package com.smashingmods.chemlib;

import com.smashingmods.chemlib.client.ElementRenderer;
import com.smashingmods.chemlib.registry.BlockRegistry;
import com.smashingmods.chemlib.registry.FluidRegistry;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;

/**
 * Client side mod initializer for registering dynamic renderers.
 *
 * @author TechnoVision
 */
public class ChemLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register item colors
        ItemRegistry.getElements().forEach(element -> ColorProviderRegistry.ITEM.register(element::getColor, element));
        ItemRegistry.getCompounds().forEach(compound -> ColorProviderRegistry.ITEM.register(compound::getColor, compound));
        ItemRegistry.getChemicalItems().forEach(item -> ColorProviderRegistry.ITEM.register(item::getColor, item));
        ItemRegistry.getChemicalBlockItems().forEach(item -> ColorProviderRegistry.ITEM.register(item::getColor, item));

        // Register fluid and bucket colors
        FluidRegistry.getBuckets().forEach(bucket -> ColorProviderRegistry.ITEM.register((pStack, pTintIndex) -> pTintIndex == 0 ? bucket.getColor() : -1, bucket.asItem()));
        FluidRegistry.FLUIDS.forEach(fluid -> {
            // Register renderer for still/flowing animation
            FluidRenderHandlerRegistry.INSTANCE.register(fluid, new SimpleFluidRenderHandler(
                    SimpleFluidRenderHandler.WATER_STILL,
                    SimpleFluidRenderHandler.WATER_FLOWING,
                    SimpleFluidRenderHandler.WATER_OVERLAY,
                    fluid.getColor())
            );
            // Make fluid translucent
            BlockRenderLayerMap.INSTANCE.putFluid(fluid, RenderLayer.getTranslucent());
        });

        // Register block colors
        BlockRegistry.getAllChemicalBlocks().forEach((block) -> ColorProviderRegistry.BLOCK.register(block.getBlockColor(new ItemStack(block.asItem()), 0), block));

        // Register dynamic item renderer for elements
        ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, identifierConsumer) -> {
            identifierConsumer.accept(ElementRenderer.SOLID_MODEL_LOCATION);
            identifierConsumer.accept(ElementRenderer.LIQUID_MODEL_LOCATION);
            identifierConsumer.accept(ElementRenderer.GAS_MODEL_LOCATION);
        });
        ItemRegistry.getElements().forEach(element -> BuiltinItemRendererRegistry.INSTANCE.register(element, new ElementRenderer()));
    }
}
