package com.technovision.chemlib;

import com.technovision.chemlib.registry.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class ChemLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register item colors
        ItemRegistry.ELEMENTS.forEach(element -> ColorProviderRegistry.ITEM.register(element::getColor, element));
        ItemRegistry.COMPOUNDS.forEach(element -> ColorProviderRegistry.ITEM.register(element::getColor, element));
    }
}
