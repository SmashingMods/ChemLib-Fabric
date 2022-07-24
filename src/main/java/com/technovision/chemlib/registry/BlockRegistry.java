package com.technovision.chemlib.registry;

import com.technovision.chemlib.api.ChemicalBlockType;
import com.technovision.chemlib.common.blocks.ChemicalBlock;
import com.technovision.chemlib.common.blocks.LampBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.stream.Stream;

import static net.minecraft.state.property.Properties.LIT;

public class BlockRegistry {

    public static final Map<String, ChemicalBlock> BLOCKS = new HashMap<>();
    public static final List<ChemicalBlock> METAL_BLOCKS = new ArrayList<>();
    public static final List<ChemicalBlock> LAMP_BLOCKS = new ArrayList<>();

    public static final FabricBlockSettings METAL_PROPERTIES = FabricBlockSettings.of(Material.METAL).strength(5.0f, 12.0f).sounds(BlockSoundGroup.METAL);

    public static final FabricBlockSettings LAMP_PROPERTIES = FabricBlockSettings.of(Material.GLASS).strength(2.0f, 2.0f).sounds(BlockSoundGroup.GLASS).luminance(state -> state.get(LIT) ? 15 : 0);

    public static Block getBlockByName(String name) {
        return BLOCKS.get(name);
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

    public static ChemicalBlock registerBlock(Identifier chemicalIdentifier, Identifier blockIdentifier, ChemicalBlockType type) {
        ChemicalBlock chemicalBlock;
        FabricBlockSettings settings;
        if (type == ChemicalBlockType.METAL) {
            settings = METAL_PROPERTIES;
            chemicalBlock = new ChemicalBlock(chemicalIdentifier, type, settings);
        } else {
            settings = LAMP_PROPERTIES;
            chemicalBlock = new LampBlock(chemicalIdentifier, type, settings);
        }

        Registry.register(Registry.BLOCK, blockIdentifier, chemicalBlock);
        BLOCKS.put(blockIdentifier.getPath(), chemicalBlock);
        if (type == ChemicalBlockType.METAL) {
            METAL_BLOCKS.add(chemicalBlock);
        } else {
            LAMP_BLOCKS.add(chemicalBlock);
        }
        return chemicalBlock;
    }
}
