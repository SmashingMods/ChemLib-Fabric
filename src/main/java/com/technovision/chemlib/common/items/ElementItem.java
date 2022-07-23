package com.technovision.chemlib.common.items;

import com.technovision.chemlib.ChemLib;
import com.technovision.chemlib.api.Element;
import com.technovision.chemlib.api.MatterState;
import com.technovision.chemlib.api.MetalType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ElementItem extends Item implements Element {

    private final String elementName;
    private final int atomicNumber;
    private final String abbreviation;
    private final int group;
    private final int period;
    private final MatterState matterState;
    private final MetalType metalType;
    private final boolean artificial;
    private final int color;

    public ElementItem(String pChemicalName, int pAtomicNumber, String pAbbreviation, int pGroup, int pPeriod, MatterState pMatterState, MetalType pMetalType, boolean pArtificial, String pColor) {
        super(new FabricItemSettings().group(ChemLib.ELEMENTS_TAB));
        this.elementName = pChemicalName;
        this.atomicNumber = pAtomicNumber;
        this.abbreviation = pAbbreviation;
        this.group = pGroup;
        this.period = pPeriod;
        this.matterState = pMatterState;
        this.metalType = pMetalType;
        this.artificial = pArtificial;
        this.color = (int) Long.parseLong(pColor, 16);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.literal(String.format("%s (%d)", getAbbreviation(), atomicNumber)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
        if (!getGroupName().isEmpty()) {
            tooltip.add(Text.literal(getGroupName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
    }

    @Override
    public String getChemicalName() {
        return this.elementName;
    }

    @Override
    public int getAtomicNumber() {
        return this.atomicNumber;
    }

    @Override
    public String getAbbreviation() {
        return this.abbreviation;
    }

    @Override
    public int getPeriod() {
        return period;
    }

    @Override
    public int getElementGroup() {
        return group;
    }

    public String getGroupName() {
        return switch(group) {
            case 1 -> atomicNumber != 1 ? "Alkali Metal" : "";
            case 2 -> "Aklaline Earth Metal";
            case 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 -> switch(period) {
                case 6 -> "Lanthanide";
                case 7 -> "Actinide";
                default -> "Transition Metal";
            };
            case 13 -> "Boron Group";
            case 14 -> "Carbon Group";
            case 15 -> "Nitrogen Group";
            case 16 -> "Chalcogen";
            case 17 -> "Halogen";
            case 18 -> "Noble Gas";
            default -> "";
        };
    }

    @Override
    public MatterState getMatterState() {
        return matterState;
    }

    @Override
    public MetalType getMetalType() {
        return metalType;
    }

    @Override
    public boolean isArtificial() {
        return artificial;
    }

    @Override
    public String getChemicalDescription() {
        return "";
    }

    @Override
    public int getColor() {
        return this.color;
    }

    public int getColor(ItemStack pItemStack, int pTintIndex) {
        return pTintIndex > 0 ? -1 : color;
    }
}
