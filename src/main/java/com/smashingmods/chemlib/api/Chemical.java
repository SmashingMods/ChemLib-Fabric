package com.smashingmods.chemlib.api;

public interface Chemical {

    String getChemicalName();

    String getAbbreviation();

    MatterState getMatterState();

    String getChemicalDescription();

    int getColor();
}
