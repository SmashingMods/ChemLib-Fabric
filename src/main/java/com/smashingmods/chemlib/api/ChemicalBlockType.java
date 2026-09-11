package com.smashingmods.chemlib.api;

import net.minecraft.util.StringRepresentable;

public enum ChemicalBlockType implements StringRepresentable {
    METAL("metal"),
    LAMP("lamp");

    private final String type;

    ChemicalBlockType(String pType) {
        this.type = pType;
    }

    public String asString() { return getSerializedName(); }

    @Override
    public String getSerializedName() {
        return type;
    }
}
