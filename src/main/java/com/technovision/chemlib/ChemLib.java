package com.technovision.chemlib;

import com.technovision.chemlib.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
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

    public static final ItemGroup ELEMENTS_TAB = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "elements"),
            () -> new ItemStack(Items.BLAZE_POWDER)
    );
    public static final ItemGroup COMPOUNDS_TAB = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "compounds"),
            () -> new ItemStack(Items.LAVA_BUCKET)
    );
    public static final ItemGroup METALS_TAB = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "metals"),
            () -> new ItemStack(Items.IRON_BLOCK)
    );
    public static final ItemGroup MISC_TAB = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "misc"),
            () -> new ItemStack(Items.REDSTONE_LAMP)
    );

    @Override
    public void onInitialize() {
        //BlockRegistry.register();
        //FluidRegistry.register();
        //PaintingsRegistry.register();
        try {
            ItemRegistry.register();
        } catch (IOException e) {
            LOGGER.error("Unable to access compounds.json or elements.json");
        }
    }
}
