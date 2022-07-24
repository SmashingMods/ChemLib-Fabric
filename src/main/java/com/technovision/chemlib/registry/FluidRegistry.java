package com.technovision.chemlib.registry;

import com.technovision.chemlib.ChemLib;
import com.technovision.chemlib.common.fluids.ChemicalFluid;
import com.technovision.chemlib.common.fluids.FluidAttributes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class FluidRegistry {

    public static final Identifier STILL = new Identifier("block/water_still");
    public static final Identifier FLOWING = new Identifier("block/water_flow");
    public static final Identifier OVERLAY = new Identifier("block/water_overlay");

    public static final List<FlowableFluid> FLUIDS = new ArrayList<>();
    public static final List<FluidBlock> LIQUID_BLOCKS = new ArrayList<>();
    public static final List<BucketItem> BUCKETS = new ArrayList<>();

    protected static void registerFluid(String pName, FluidAttributes attributes, int pSlopeFindDistance, int pDecreasePerBlock) {

        var ref = new Object() {
            ChemicalFluid.Properties properties = null;
        };

        ChemicalFluid fluidSource = new ChemicalFluid.Still(ref.properties);
        ChemicalFluid fluidFlowing = new ChemicalFluid.Flowing(ref.properties);
        ref.properties = new ChemicalFluid.Properties(() -> fluidSource, () -> fluidFlowing, attributes);
        fluidSource.updateProperties(ref.properties);
        fluidFlowing.updateProperties(ref.properties);

        FluidBlock liquidBlock = new FluidBlock(fluidSource, FabricBlockSettings.of(Material.WATER).noCollision().strength(100f).dropsNothing());
        BucketItem bucket = new BucketItem(fluidSource, new FabricItemSettings().recipeRemainder(Items.BUCKET).maxCount(1));

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

        Registry.register(Registry.FLUID, new Identifier(ChemLib.MOD_ID, String.format("%s_source", pName)), fluidSource);
        Registry.register(Registry.FLUID, new Identifier(ChemLib.MOD_ID, String.format("%s_flowing", pName)), fluidFlowing);
        Registry.register(Registry.BLOCK, new Identifier(ChemLib.MOD_ID, String.format("%s_liquid_block", pName)), liquidBlock);
        Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_bucket", pName)), bucket);
        ItemRegistry.registerLiquidBlock(liquidBlock, new FabricItemSettings().group(ItemRegistry.MISC_TAB));
    }

    public static List<FluidBlock> getLiquidBlocks() {
        return LIQUID_BLOCKS;
    }

    public static List<BucketItem> getBuckets() {
        return BUCKETS;
    }

}
