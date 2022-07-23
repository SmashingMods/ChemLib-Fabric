package com.technovision.chemlib;

import com.technovision.chemlib.client.ElementRenderer;
import com.technovision.chemlib.registry.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public class ChemLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register item colors
        ItemRegistry.ELEMENTS.forEach(element -> ColorProviderRegistry.ITEM.register(element::getColor, element));
        ItemRegistry.COMPOUNDS.forEach(element -> ColorProviderRegistry.ITEM.register(element::getColor, element));

        // Register dynamic item renderer for elements
        ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, identifierConsumer) -> {
            identifierConsumer.accept(ElementRenderer.SOLID_MODEL_LOCATION);
            identifierConsumer.accept(ElementRenderer.LIQUID_MODEL_LOCATION);
            identifierConsumer.accept(ElementRenderer.GAS_MODEL_LOCATION);
        });
        ItemRegistry.ELEMENTS.forEach(element -> BuiltinItemRendererRegistry.INSTANCE.register(element, new ElementRenderer()));
    }
}
