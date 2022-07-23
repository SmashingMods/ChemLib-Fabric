package com.technovision.chemlib.api;

import net.minecraft.util.StringIdentifiable;

public enum MatterState implements StringIdentifiable {
    SOLID("solid"),
    LIQUID("liquid"),
    GAS("gas");

    private final String state;

    MatterState(String pState) {
        this.state = pState;
    }

    @Override
    public String asString() {
        return state;
    }
}
