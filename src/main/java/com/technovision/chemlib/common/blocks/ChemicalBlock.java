package com.technovision.chemlib.common.blocks;

import com.technovision.chemlib.api.Chemical;
import com.technovision.chemlib.api.ChemicalBlockType;
import com.technovision.chemlib.api.MatterState;
import com.technovision.chemlib.registry.ItemRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ChemicalBlock extends Block implements Chemical {

    private final Identifier chemical;
    private final ChemicalBlockType blockType;

    public ChemicalBlock(Identifier chemical, ChemicalBlockType blockType, FabricBlockSettings properties) {
        super(properties);
        this.chemical = chemical;
        this.blockType = blockType;
    }

    public Chemical getChemical() {
        return (Chemical) Registry.ITEM.get(chemical);
    }

    public ChemicalBlockType getBlockType() {
        return blockType;
    }

    @Override
    public String getChemicalName() {
        return getChemical().getChemicalName();
    }

    @Override
    public String getAbbreviation() {
        return getChemical().getAbbreviation();
    }

    @Override
    public MatterState getMatterState() {
        return MatterState.SOLID;
    }

    @Override
    public String getChemicalDescription() {
        return "";
    }

    @Override
    public int getColor() {
        return getChemical().getColor();
    }

    public BlockColorProvider getBlockColor(ItemStack pItemStack, int pTintIndex) {
        return (pState, pLevel, pPos, pTintIndex1) -> getChemical().getColor();
    }
}
