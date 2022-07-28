package com.smashingmods.chemlib.api;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Stores attributes for a custom fluid type
 *
 * @author TechnoVision
 */
public class FluidAttributes {

    public final Identifier stillTexture;
    public final Identifier flowingTexture;
    public Identifier overlayTexture;
    public int color = 0xFFFFFFFF;
    public SoundEvent fillSound;
    public SoundEvent emptySound;
    public int luminosity = 0;
    public int density = 1000;
    public int viscosity = 1000;
    public boolean isGaseous;

    public FluidAttributes(Identifier stillTexture, Identifier flowingTexture) {
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
    }

    public final FluidAttributes color(int color)
    {
        this.color = color;
        return this;
    }

    public final FluidAttributes overlay(Identifier texture)
    {
        overlayTexture = texture;
        return this;
    }

    public final FluidAttributes luminosity(int luminosity)
    {
        this.luminosity = luminosity;
        return this;
    }

    public final FluidAttributes density(int density)
    {
        this.density = density;
        return this;
    }

    public final FluidAttributes viscosity(int viscosity)
    {
        this.viscosity = viscosity;
        return this;
    }

    public final FluidAttributes gaseous()
    {
        isGaseous = true;
        return this;
    }

    public final FluidAttributes sound(SoundEvent sound)
    {
        this.fillSound = this.emptySound = sound;
        return this;
    }
}
