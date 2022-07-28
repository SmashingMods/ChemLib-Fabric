package com.smashingmods.chemlib.api;

import net.minecraft.util.StringIdentifiable;

public enum MetalType implements StringIdentifiable {
    METAL("metal"),
    METALLOID("metalloid"),
    NONMETAL("nonmetal");

    private final String state;

    MetalType(String pState) {
        this.state = pState;
    }

    @Override
    public String asString() {
        return state;
    }
}
