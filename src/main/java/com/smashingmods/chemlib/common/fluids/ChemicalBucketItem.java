package com.smashingmods.chemlib.common.fluids;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;

public class ChemicalBucketItem extends BucketItem {

    private final int color;

    public ChemicalBucketItem(Fluid fluid, Settings settings, int color) {
        super(fluid, settings);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
