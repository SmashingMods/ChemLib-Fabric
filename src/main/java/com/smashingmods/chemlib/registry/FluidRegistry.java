package com.smashingmods.chemlib.registry;

import com.smashingmods.chemlib.ChemLib;
import com.smashingmods.chemlib.common.fluids.ChemicalFluid;
import com.smashingmods.chemlib.common.fluids.ChemicalFluidBlock;
import com.smashingmods.chemlib.common.fluids.ChemicalBucketItem;
import com.smashingmods.chemlib.api.FluidAttributes;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.List;

public class FluidRegistry {

    public static final Identifier STILL = Identifier.withDefaultNamespace("block/water_still");
    public static final Identifier FLOWING = Identifier.withDefaultNamespace("block/water_flow");
    public static final Identifier OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");

    public static final List<ChemicalFluid> FLUIDS = new ArrayList<>();
    public static final List<ChemicalFluidBlock> LIQUID_BLOCKS = new ArrayList<>();
    public static final List<ChemicalBucketItem> BUCKETS = new ArrayList<>();

    protected static void registerFluid(String name, FluidAttributes attributes, int pSlopeFindDistance, int pDecreasePerBlock) {

        var ref = new Object() {
            ChemicalFluid.Properties properties = null;
        };

        ChemicalFluid fluidSource = new ChemicalFluid.Still(ref.properties);
        ChemicalFluid fluidFlowing = new ChemicalFluid.Flowing(ref.properties);
        ref.properties = new ChemicalFluid.Properties(() -> fluidSource, () -> fluidFlowing, attributes);
        fluidSource.updateProperties(ref.properties);
        fluidFlowing.updateProperties(ref.properties);

        ChemicalFluidBlock liquidBlock = new ChemicalFluidBlock(fluidSource, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, name + "_liquid_block"))), attributes.color);
        ChemicalBucketItem bucket = new ChemicalBucketItem(fluidSource, ItemRegistry.itemProperties(name + "_bucket").craftRemainder(Items.BUCKET).stacksTo(1), attributes.color);

        ref.properties.slopeFindDistance(pSlopeFindDistance)
                .levelDecreasePerBlock(pDecreasePerBlock)
                .block(() -> liquidBlock)
                .bucket(() -> bucket);
        fluidSource.updateProperties(ref.properties);
        fluidFlowing.updateProperties(ref.properties);

        FLUIDS.add(fluidSource);
        FLUIDS.add(fluidFlowing);
        LIQUID_BLOCKS.add(liquidBlock);
        BUCKETS.add(bucket);

        Registry.register(BuiltInRegistries.FLUID, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, String.format("%s_source", name)), fluidSource);
        Registry.register(BuiltInRegistries.FLUID, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, String.format("%s_flowing", name)), fluidFlowing);
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, String.format("%s_liquid_block", name)), liquidBlock);
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(ChemLib.MOD_ID, String.format("%s_bucket", name)), bucket);
    }

    public static List<ChemicalFluid> getFluids() {
        return FLUIDS;
    }

    public static List<ChemicalFluidBlock> getFluidBlocks() {
        return LIQUID_BLOCKS;
    }

    public static List<ChemicalBucketItem> getBuckets() {
        return BUCKETS;
    }

}
