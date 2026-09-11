package com.smashingmods.chemlib.common.blocks;

import com.smashingmods.chemlib.api.ChemicalBlockType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public class LampBlock extends ChemicalBlock {

    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    public LampBlock(Identifier chemical, ChemicalBlockType type, BlockBehaviour.Properties properties) {
        super(chemical, type, properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(LIT, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, net.minecraft.world.level.redstone.Orientation orientation, boolean notify) {
        super.neighborChanged(state, world, pos, sourceBlock, orientation, notify);
        if (!world.isClientSide()) {
            boolean flag = state.getValue(LIT);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag) {
                    world.scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(LIT), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource random) {
        super.tick(state, world, pos, random);
        if (state.getValue(LIT) && !world.hasNeighborSignal(pos)) {
            world.setBlock(pos, state.cycle(LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }
}
