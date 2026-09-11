package com.smashingmods.chemlib.registry;

import com.smashingmods.chemlib.api.ChemicalBlockType;
import com.smashingmods.chemlib.common.blocks.ChemicalBlock;
import com.smashingmods.chemlib.common.blocks.LampBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.*;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class BlockRegistry {

    public static final List<ChemicalBlock> METAL_BLOCKS = new ArrayList<>();
    public static final List<ChemicalBlock> LAMP_BLOCKS = new ArrayList<>();

    public static final BlockBehaviour.Properties METAL_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f, 12.0f).sound(SoundType.METAL).requiresCorrectToolForDrops();

    public static final BlockBehaviour.Properties LAMP_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(2.0f, 2.0f).sound(SoundType.GLASS).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(LIT) ? 15 : 0);

    public static Optional<ChemicalBlock> getChemicalBlockByName(String name) {
        return getAllChemicalBlocks().stream().filter(blockRegistryObject -> blockRegistryObject.toString().equals(name)).findFirst();
    }

    public static List<ChemicalBlock> getMetalBlocks() {
        return METAL_BLOCKS;
    }

    public static List<ChemicalBlock> getLampBlocks() {
        return LAMP_BLOCKS;
    }

    public static List<ChemicalBlock> getAllChemicalBlocks() {
        List<ChemicalBlock> all = new ArrayList<>();
        all.addAll(METAL_BLOCKS);
        all.addAll(LAMP_BLOCKS);
        return all;
    }

    public static List<ChemicalBlock> getChemicalBlocksByType(ChemicalBlockType pChemicalBlockType) {
        return switch (pChemicalBlockType) {
            case METAL -> METAL_BLOCKS;
            case LAMP -> LAMP_BLOCKS;
        };
    }

    public static Stream<ChemicalBlock> getChemicalBlocksStreamByType(ChemicalBlockType pChemicalBlockType) {
        return getChemicalBlocksByType(pChemicalBlockType)
                .stream().filter(block -> block.getBlockType().equals(pChemicalBlockType));
    }

    public static Optional<ChemicalBlock> getChemicalBlockByNameAndType(String pName, ChemicalBlockType pChemicalBlockType) {
        return getChemicalBlocksStreamByType(pChemicalBlockType)
                .filter(block -> block.getChemical().getChemicalName().equals(pName))
                .findFirst();
    }

    protected static ChemicalBlock registerBlock(Identifier chemicalIdentifier, Identifier blockIdentifier, ChemicalBlockType type) {
        ChemicalBlock chemicalBlock;
        BlockBehaviour.Properties settings;
        if (type == ChemicalBlockType.METAL) {
            settings = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f, 12.0f).sound(SoundType.METAL).requiresCorrectToolForDrops().setId(ResourceKey.create(Registries.BLOCK, blockIdentifier));
            chemicalBlock = new ChemicalBlock(chemicalIdentifier, type, settings);
            METAL_BLOCKS.add(chemicalBlock);
        } else {
            settings = BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(2.0f, 2.0f).sound(SoundType.GLASS).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(LIT) ? 15 : 0).setId(ResourceKey.create(Registries.BLOCK, blockIdentifier));
            chemicalBlock = new LampBlock(chemicalIdentifier, type, settings);
            LAMP_BLOCKS.add(chemicalBlock);
        }
        Registry.register(BuiltInRegistries.BLOCK, blockIdentifier, chemicalBlock);
        return chemicalBlock;
    }
}
