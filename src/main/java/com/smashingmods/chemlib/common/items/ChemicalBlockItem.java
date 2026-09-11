package com.smashingmods.chemlib.common.items;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.ChemicalBlockType;
import com.smashingmods.chemlib.api.Element;
import com.smashingmods.chemlib.api.MatterState;
import com.smashingmods.chemlib.common.blocks.ChemicalBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class ChemicalBlockItem extends BlockItem implements Chemical {

    private final ChemicalBlock block;
    private final ChemicalBlockType type;

    public ChemicalBlockItem(ChemicalBlock block, Item.Properties settings) {
        super(block, settings);
        this.block = block;
        this.type = block.getBlockType();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        if (getChemical() instanceof Element element) {
            tooltip.accept(Component.literal(String.format("%s (%d)", getAbbreviation(), element.getAtomicNumber())).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
            tooltip.accept(Component.literal(element.getGroupName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }

    public Chemical getChemical() {
        return block.getChemical();
    }

    @Override
    public String getChemicalName() {
        return block.getChemicalName();
    }

    @Override
    public String getAbbreviation() {
        return getChemical().getAbbreviation();
    }

    @Override
    public MatterState getMatterState() {
        return getChemical().getMatterState();
    }

    @Override
    public String getChemicalDescription() {
        return getChemical().getChemicalDescription();
    }

    @Override
    public int getColor() {
        return getChemical().getColor();
    }

    public int getColor(ItemStack pItemStack, int pTintIndex) {
        return getColor();
    }

    public ChemicalBlockType getType() {
        return type;
    }
}
