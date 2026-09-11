package com.smashingmods.chemlib.common.fluids;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.BucketItem;

public class ChemicalBucketItem extends BucketItem {

    private final int color;

    public ChemicalBucketItem(Fluid fluid, Properties settings, int color) {
        super(fluid, settings);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
