package com.technovision.chemlib.common.blocks;

import com.technovision.chemlib.api.Chemical;
import com.technovision.chemlib.api.ChemicalBlockType;
import com.technovision.chemlib.api.MatterState;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.List;

public class ChemicalBlock extends Block implements Chemical {

    private final Identifier chemical;
    private final ChemicalBlockType blockType;

    public ChemicalBlock(Identifier pChemical, ChemicalBlockType pBlockType, List<ChemicalBlock> pList, FabricBlockSettings pProperties) {
        super(pProperties);
        this.chemical = pChemical;
        this.blockType = pBlockType;
        pList.add(this);
    }

    public Chemical getChemical() {
        // TODO: Fix
        //return (Chemical) ForgeRegistries.ITEMS.getValue(chemical);
        return null;
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

    // TODO: Fix
    /**
    public BlockColor getBlockColor(ItemStack pItemStack, int pTintIndex) {
        return (pState, pLevel, pPos, pTintIndex1) -> getChemical().getColor();
    }
     */
}
