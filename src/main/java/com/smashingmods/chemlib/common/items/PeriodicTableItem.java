package com.smashingmods.chemlib.common.items;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.smashingmods.chemlib.client.PeriodicTableScreen;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PeriodicTableItem extends Item {

    public PeriodicTableItem() {
        super(new FabricItemSettings().group(ItemRegistry.MISC_TAB).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            MinecraftClient.getInstance().setScreen(new PeriodicTableScreen());
        }
        return  TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.chemlib.periodic_table.tooltip").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
    }
}
