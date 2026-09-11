package com.smashingmods.chemlib.common.blocks;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.ChemicalBlockType;
import com.smashingmods.chemlib.api.MatterState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

public class ChemicalBlock extends Block implements Chemical {

    private final Identifier chemical;
    private final ChemicalBlockType blockType;

    public ChemicalBlock(Identifier chemical, ChemicalBlockType blockType, BlockBehaviour.Properties properties) {
        super(properties);
        this.chemical = chemical;
        this.blockType = blockType;
    }

    public Chemical getChemical() {
        return (Chemical) BuiltInRegistries.ITEM.getValue(chemical);
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

}
