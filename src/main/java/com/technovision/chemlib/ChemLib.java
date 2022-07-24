package com.technovision.chemlib;

import com.technovision.chemlib.registry.ItemRegistry;
import com.technovision.chemlib.registry.PaintingRegistry;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Mod library that adds periods elements
 * and compounds as items in Minecraft.
 *
 * @version 1.0.0
 * @author TecnoVisions
 */
public class ChemLib implements ModInitializer {

    public static final String MOD_ID = "chemlib";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        try {
            ItemRegistry.register();
            PaintingRegistry.register();
        } catch (IOException e) {
            LOGGER.error("Unable to access compounds.json or elements.json");
        }
    }
}
