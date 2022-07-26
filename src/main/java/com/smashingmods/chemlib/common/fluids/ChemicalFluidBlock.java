package com.smashingmods.chemlib.common.fluids;

import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;

public class ChemicalFluidBlock extends FluidBlock {

    private final int color;

    public ChemicalFluidBlock(FlowableFluid fluid, Settings settings, int color) {
        super(fluid, settings);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
