package com.smashingmods.chemlib.common.fluids;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class ChemicalFluidBlock extends LiquidBlock {

    private final int color;

    public ChemicalFluidBlock(FlowingFluid fluid, Properties settings, int color) {
        super(fluid, settings);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
