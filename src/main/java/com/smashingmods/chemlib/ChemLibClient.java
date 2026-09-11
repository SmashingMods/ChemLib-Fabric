package com.smashingmods.chemlib;

import com.smashingmods.chemlib.client.ElementRenderer;
import com.smashingmods.chemlib.registry.BlockRegistry;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ChemLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemModels.ID_MAPPER.put(Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, "element"), ElementRenderer.Unbaked.MAP_CODEC);
        FluidRegistry.FLUIDS.forEach(fluid -> FluidRenderingRegistry.register(fluid, new FluidModel.Unbaked(
                new Material(FluidRegistry.STILL, true),
                new Material(FluidRegistry.FLOWING, true),
                new Material(FluidRegistry.OVERLAY, true),
                state -> fluid.getColor())));
        BlockRegistry.getAllChemicalBlocks().forEach(block -> BlockColorRegistry.register(
                List.<BlockTintSource>of(state -> block.getColor()), block));
    }
}
