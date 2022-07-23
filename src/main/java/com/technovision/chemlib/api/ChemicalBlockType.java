package com.technovision.chemlib.api;

import net.minecraft.util.StringIdentifiable;

public enum ChemicalBlockType implements StringIdentifiable {
    METAL("metal"),
    LAMP("lamp");

    private final String type;

    ChemicalBlockType(String pType) {
        this.type = pType;
    }

    @Override
    public String asString() {
        return type;
    }
}
