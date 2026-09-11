package com.smashingmods.chemlib.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smashingmods.chemlib.registry.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ChemLibGameTest {
    private static Identifier id(String path) { return Identifier.fromNamespaceAndPath("chemlib", path); }

    @GameTest
    public void contentAndData(GameTestHelper test) {
        JsonObject manifest = JsonParser.parseReader(new InputStreamReader(
                getClass().getResourceAsStream("/chemlib-content-manifest.json"), StandardCharsets.UTF_8)).getAsJsonObject();
        manifest.getAsJsonArray("items").forEach(value -> test.assertTrue(
                BuiltInRegistries.ITEM.containsKey(id(value.getAsString())), "Missing item: " + value));
        manifest.getAsJsonArray("blocks").forEach(value -> test.assertTrue(
                BuiltInRegistries.BLOCK.containsKey(id(value.getAsString())), "Missing block: " + value));
        manifest.getAsJsonArray("recipes").forEach(value -> test.assertTrue(
                test.getLevel().getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id(value.getAsString()))).isPresent(),
                "Missing recipe: " + value));
        manifest.getAsJsonArray("advancements").forEach(value -> test.assertTrue(
                test.getLevel().getServer().getAdvancements().get(id(value.getAsString())) != null, "Missing advancement: " + value));
        test.assertTrue(ItemRegistry.ELEMENTS.size() == 118, "All 118 elements must remain available");
        test.assertTrue(ItemRegistry.COMPOUNDS.size() == 175, "All 175 compounds must remain available");
        var painting = test.getLevel().registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT)
                .getOrThrow(PaintingRegistry.PERIODIC_TABLE_PAINTING).value();
        test.assertTrue(painting.width() == 5 && painting.height() == 3, "Painting must remain 5 by 3 blocks");
        for (var fluid : FluidRegistry.FLUIDS) {
            test.assertTrue(fluid.getBucket() != null, "Fluid must have a bucket");
            test.assertTrue(fluid.isSame(fluid.getSource()) && fluid.isSame(fluid.getFlowing()), "Fluid pair must match");
            test.assertTrue(fluid.getTickDelay(test.getLevel()) == 5, "Fluid tick delay changed");
            test.assertTrue(fluid.defaultFluidState().createLegacyBlock().getFluidState().getType().isSame(fluid), "Fluid block round trip failed");
        }
        var parameters = new net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters(
                test.getLevel().enabledFeatures(), true, test.getLevel().registryAccess());
        var tabs = java.util.List.of(ItemRegistry.ELEMENTS_TAB, ItemRegistry.COMPOUNDS_TAB, ItemRegistry.METALS_TAB, ItemRegistry.MISC_TAB);
        java.util.Set<net.minecraft.world.item.Item> tabItems = new java.util.HashSet<>();
        for (var tab : tabs) {
            tab.buildContents(parameters);
            test.assertTrue(!tab.getIconItem().isEmpty(), "Creative tab icon missing");
            tab.getDisplayItems().forEach(stack -> test.assertTrue(tabItems.add(stack.getItem()), "Duplicate creative item: " + stack));
        }
        test.assertTrue(tabItems.size() == 800, "All 800 items must appear exactly once across the four tabs");
        for (var block : BlockRegistry.getAllChemicalBlocks()) {
            var drops = net.minecraft.world.level.block.Block.getDrops(block.defaultBlockState(), test.getLevel(),
                    test.absolutePos(new BlockPos(1, 1, 1)), null, null, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_PICKAXE));
            test.assertTrue(drops.size() == 1 && drops.getFirst().is(block.asItem()) && drops.getFirst().getCount() == 1,
                    "Block must drop itself: " + block);
            test.assertTrue(!new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_PICKAXE).isCorrectToolForDrops(block.defaultBlockState()), "Wooden pickaxe must not harvest metal/lamp blocks");
            test.assertTrue(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE).isCorrectToolForDrops(block.defaultBlockState()), "Stone pickaxe must harvest metal/lamp blocks");
        }
        test.succeed();
    }

    @GameTest
    public void bucketsAndFluidFlow(GameTestHelper test) {
        BlockPos source = new BlockPos(2, 2, 2);
        BlockPos absolute = test.absolutePos(source);
        for (int i = 0; i < FluidRegistry.BUCKETS.size(); i++) {
            var bucket = FluidRegistry.BUCKETS.get(i);
            var block = FluidRegistry.LIQUID_BLOCKS.get(i);
            test.assertTrue(bucket.emptyContents(null, test.getLevel(), absolute, null), "Bucket must place its fluid");
            test.assertTrue(test.getBlockState(source).is(block), "Bucket placed the wrong fluid block");
            var pickedUp = block.pickupBlock(null, test.getLevel(), absolute, test.getBlockState(source));
            test.assertTrue(pickedUp.is(bucket), "Fluid must return its matching bucket");
            test.assertTrue(test.getBlockState(source).isAir(), "Picking up the source must remove it");
        }
        for (int x = 0; x <= 4; x++) {
            for (int z = 0; z <= 4; z++) test.setBlock(new BlockPos(x, 1, z), Blocks.STONE);
        }
        var fluid = FluidRegistry.FLUIDS.getFirst();
        test.setBlock(source, fluid.defaultFluidState().createLegacyBlock());
        test.runAfterDelay(10, () -> {
            var flowing = test.getBlockState(source.east()).getFluidState();
            test.assertTrue(flowing.getType().isSame(fluid), "Fluid must spread across the floor");
            test.assertTrue(!flowing.isSource(), "Flowing fluid must not become a source");
            test.assertTrue(flowing.getAmount() == 8 - fluid.getDropOff(test.getLevel()), "Flow level decrease changed");
            test.succeed();
        });
    }

    @GameTest
    public void lampRedstoneDelay(GameTestHelper test) {
        var lamp = BlockRegistry.getLampBlocks().getFirst();
        BlockPos pos = new BlockPos(1, 2, 1);
        BlockPos power = pos.below();
        test.setBlock(pos, lamp);
        test.setBlock(power, Blocks.REDSTONE_BLOCK);
        test.runAfterDelay(1, () -> {
            test.assertTrue(test.getBlockState(pos).getValue(BlockStateProperties.LIT), "Powered lamp must light");
            test.assertTrue(test.getBlockState(pos).getLightEmission() == 15, "Powered lamp must emit level 15");
            test.setBlock(power, Blocks.AIR);
            test.assertTrue(test.getBlockState(pos).getValue(BlockStateProperties.LIT), "Lamp must not turn off immediately");
            test.runAfterDelay(3, () -> test.assertTrue(test.getBlockState(pos).getValue(BlockStateProperties.LIT), "Lamp must remain lit for four ticks"));
            test.runAfterDelay(5, () -> {
                test.assertTrue(!test.getBlockState(pos).getValue(BlockStateProperties.LIT), "Unpowered lamp must turn off");
                test.succeed();
            });
        });
    }
}
