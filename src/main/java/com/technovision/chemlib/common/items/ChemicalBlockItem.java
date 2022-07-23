package com.technovision.chemlib.common.items;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.technovision.chemlib.api.Chemical;
import com.technovision.chemlib.api.Element;
import com.technovision.chemlib.api.MatterState;
import com.technovision.chemlib.common.blocks.ChemicalBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChemicalBlockItem extends BlockItem implements Chemical {

    private final ChemicalBlock block;

    public ChemicalBlockItem(ChemicalBlock block, FabricItemSettings settings) {
        super(block, settings);
        this.block = block;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (getChemical() instanceof Element element) {
            tooltip.add(Text.literal(String.format("%s (%d)", getAbbreviation(), element.getAtomicNumber())).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
            tooltip.add(Text.literal(element.getGroupName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
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
}
