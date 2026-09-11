package com.smashingmods.chemlib.common.items;

import com.smashingmods.chemlib.client.PeriodicTableScreen;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

public class PeriodicTableItem extends Item {

    public PeriodicTableItem() {
        super(ItemRegistry.itemProperties("periodic_table").stacksTo(1));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide()) {
            Minecraft.getInstance().gui.setScreen(new PeriodicTableScreen());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("item.chemlib.periodic_table.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
