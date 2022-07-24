package com.technovision.chemlib;

import com.technovision.chemlib.client.ElementRenderer;
import com.technovision.chemlib.registry.BlockRegistry;
import com.technovision.chemlib.registry.FluidRegistry;
import com.technovision.chemlib.registry.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;

public class ChemLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register item colors
        ItemRegistry.getElements().forEach(element -> ColorProviderRegistry.ITEM.register(element::getColor, element));
        ItemRegistry.getCompounds().forEach(compound -> ColorProviderRegistry.ITEM.register(compound::getColor, compound));
        ItemRegistry.getChemicalItems().forEach(item -> ColorProviderRegistry.ITEM.register(item::getColor, item));
        ItemRegistry.getChemicalBlockItems().forEach(item -> ColorProviderRegistry.ITEM.register(item::getColor, item));
        //FluidRegistry.getBuckets().forEach(bucket -> colorHandlerEvent.getItemColors().register((pStack, pTintIndex) -> pTintIndex == 0 ? bucket.getFluid().getAttributes().getColor() : -1, bucket.asItem()));
        //ItemRegistry.getLiquidBlockItems().forEach(item -> ColorProviderRegistry.ITEM.register((pStack, pTintIndex) -> pTintIndex == 0 ? ((FluidBlock) item.getBlock()).getF().getAttributes().getColor() : -1, item));

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
