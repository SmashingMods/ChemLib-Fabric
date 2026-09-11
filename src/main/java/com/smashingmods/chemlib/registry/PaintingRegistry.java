package com.smashingmods.chemlib.registry;

import com.smashingmods.chemlib.ChemLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;

public class PaintingRegistry {
    // Painting variants are loaded from the data pack registry in modern Minecraft.
    public static final ResourceKey<PaintingVariant> PERIODIC_TABLE_PAINTING = ResourceKey.create(
            Registries.PAINTING_VARIANT, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, "periodic_table"));

    public static void register() {
        // data/chemlib/painting_variant/periodic_table.json supplies this variant.
    }
}
