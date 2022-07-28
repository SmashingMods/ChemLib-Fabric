package com.smashingmods.chemlib.registry;

import com.smashingmods.chemlib.ChemLib;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PaintingRegistry {

    public static final PaintingVariant PERIODIC_TABLE_PAINTING = new PaintingVariant(80, 48);

    public static void register() {
        Registry.register(Registry.PAINTING_VARIANT, new Identifier(ChemLib.MOD_ID, "periodic_table"), PERIODIC_TABLE_PAINTING);
    }
}
