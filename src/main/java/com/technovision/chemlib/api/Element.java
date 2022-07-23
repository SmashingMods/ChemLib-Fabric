package com.technovision.chemlib.api;

public interface Element extends Chemical {

    int getAtomicNumber();

    int getElementGroup();

    int getPeriod();

    MetalType getMetalType();

    boolean isArtificial();

    String getGroupName();
}
