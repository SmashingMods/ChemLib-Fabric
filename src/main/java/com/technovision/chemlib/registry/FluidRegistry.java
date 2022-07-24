package com.technovision.chemlib.registry;

import com.technovision.chemlib.ChemLib;
import com.technovision.chemlib.common.fluids.ChemicalFluid;
import com.technovision.chemlib.common.fluids.ChemicalFluidBlock;
import com.technovision.chemlib.common.fluids.ChemicalBucketItem;
import com.technovision.chemlib.common.fluids.FluidAttributes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.block.Material;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class FluidRegistry {

    public static final Identifier STILL = new Identifier("block/water_still");
    public static final Identifier FLOWING = new Identifier("block/water_flow");
    public static final Identifier OVERLAY = new Identifier("block/water_overlay");

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

        ChemicalFluidBlock liquidBlock = new ChemicalFluidBlock(fluidSource, FabricBlockSettings.of(Material.WATER).noCollision().strength(100f).dropsNothing(), attributes.color);
        ChemicalBucketItem bucket = new ChemicalBucketItem(fluidSource, new FabricItemSettings().group(ItemRegistry.MISC_TAB).recipeRemainder(Items.BUCKET).maxCount(1), attributes.color);

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

        Registry.register(Registry.FLUID, new Identifier(ChemLib.MOD_ID, String.format("%s_source", name)), fluidSource);
        Registry.register(Registry.FLUID, new Identifier(ChemLib.MOD_ID, String.format("%s_flowing", name)), fluidFlowing);
        Registry.register(Registry.BLOCK, new Identifier(ChemLib.MOD_ID, String.format("%s_liquid_block", name)), liquidBlock);
        Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_bucket", name)), bucket);
        ItemRegistry.registerLiquidBlock(liquidBlock, new FabricItemSettings().group(ItemRegistry.MISC_TAB));
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
