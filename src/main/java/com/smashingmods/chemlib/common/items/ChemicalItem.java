package com.smashingmods.chemlib.common.items;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.ChemicalItemType;
import com.smashingmods.chemlib.api.Element;
import com.smashingmods.chemlib.api.MatterState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class ChemicalItem extends Item implements Chemical {

    private final Chemical chemical;
    private final ChemicalItemType itemType;

    public ChemicalItem(Chemical chemical, ChemicalItemType chemicalItemType, Item.Properties properties) {
        super(properties);
        this.chemical = chemical;
        this.itemType = chemicalItemType;
    }

    public ChemicalItem(Item item, ChemicalItemType chemicalItemType, Item.Properties properties) {
        this((Chemical) item, chemicalItemType, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        if (getChemical() instanceof Element element) {
            tooltip.accept(Component.literal(String.format("%s (%d)", getAbbreviation(), element.getAtomicNumber())).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
            tooltip.accept(Component.literal(element.getGroupName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        } else {
            tooltip.accept(Component.literal(getAbbreviation()).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
        }
    }

    public Chemical getChemical() {
        return chemical;
    }

    public ChemicalItemType getItemType() {
        return itemType;
    }

    @Override
    public String getChemicalName() {
        return chemical.getChemicalName();
    }

    @Override
    public String getAbbreviation() {
        return chemical.getAbbreviation();
    }

    @Override
    public MatterState getMatterState() {
        return chemical.getMatterState();
    }

    @Override
    public String getChemicalDescription() {
        return "";
    }

    @Override
    public int getColor() {
        return chemical.getColor();
    }

    public int getColor(ItemStack pItemStack, int pTintIndex) {
        return pTintIndex == 0 ? getColor() : -1;
    }
}
