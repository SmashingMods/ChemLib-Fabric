package com.technovision.chemlib.common.items;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.technovision.chemlib.api.Chemical;
import com.technovision.chemlib.api.ChemicalItemType;
import com.technovision.chemlib.api.Element;
import com.technovision.chemlib.api.MatterState;
import com.technovision.chemlib.registry.ItemRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistryClientInit;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChemicalItem extends Item implements Chemical {

    private final Chemical chemical;
    private final ChemicalItemType itemType;

    public ChemicalItem(Chemical pChemical, ChemicalItemType pChemicalItemType, FabricItemSettings pProperties) {
        super(pProperties);
        this.chemical = pChemical;
        this.itemType = pChemicalItemType;
    }

    // TODO: FIX
    /**
    public ChemicalItem(Identifier pResourceLocation, ChemicalItemType pChemicalItemType, FabricItemSettings pProperties) {
        this((Chemical) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(pResourceLocation)), pChemicalItemType, pProperties);
    }
     */

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (getChemical() instanceof Element element) {
            tooltip.add(Text.literal(String.format("%s (%d)", getAbbreviation(), element.getAtomicNumber())).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
            tooltip.add(Text.literal(element.getGroupName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        } else {
            tooltip.add(Text.literal(getAbbreviation()).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
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
