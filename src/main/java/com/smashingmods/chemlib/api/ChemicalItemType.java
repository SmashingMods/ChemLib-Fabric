package com.smashingmods.chemlib.api;

import net.minecraft.util.StringIdentifiable;

public enum ChemicalItemType implements StringIdentifiable {
    COMPOUND("dust"),
    DUST("dust"),
    NUGGET("nugget"),
    INGOT("ingot"),
    PLATE("plate");

    private final String type;

    ChemicalItemType(String pType) {
        this.type = pType;
    }

    @Override
    public String asString() {
        return type;
    }
}
