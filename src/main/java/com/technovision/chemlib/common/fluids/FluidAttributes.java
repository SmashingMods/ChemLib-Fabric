package com.technovision.chemlib.common.fluids;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class FluidAttributes {
    public final Identifier stillTexture;
    public final Identifier flowingTexture;
    public Identifier overlayTexture;
    public int color = 0xFFFFFFFF;
    public String translationKey;
    public SoundEvent fillSound;
    public SoundEvent emptySound;
    public int luminosity = 0;
    public int density = 1000;
    public int temperature = 300;
    public int viscosity = 1000;
    public boolean isGaseous;
    public Rarity rarity = Rarity.COMMON;

    public FluidAttributes(Identifier stillTexture, Identifier flowingTexture) {
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
    }

    public final FluidAttributes translationKey(String translationKey)
    {
        this.translationKey = translationKey;
        return this;
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

    public final FluidAttributes temperature(int temperature)
    {
        this.temperature = temperature;
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

    public final FluidAttributes rarity(Rarity rarity)
    {
        this.rarity = rarity;
        return this;
    }

    public final FluidAttributes sound(SoundEvent sound)
    {
        this.fillSound = this.emptySound = sound;
        return this;
    }

    public final FluidAttributes sound(SoundEvent fillSound, SoundEvent emptySound)
    {
        this.fillSound = fillSound;
        this.emptySound = emptySound;
        return this;
    }
}
