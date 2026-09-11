package com.smashingmods.chemlib.common.fluids;

import com.smashingmods.chemlib.api.FluidAttributes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import java.util.function.Supplier;

public class ChemicalFluid extends FlowingFluid {

    private Properties properties;

    public ChemicalFluid(Properties properties) {
        this.properties = properties;
    }

    public void updateProperties(Properties properties) {
        this.properties = properties;
    }

    public int getColor() {
        return properties.color;
    }

    @Override
    public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !(Boolean)state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playLocalSound((double)pos.getX() + 0.5D,
                        (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D,
                        SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS,
                        random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F,
                        false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(),
                    (double)pos.getY() + random.nextDouble(),
                    (double)pos.getZ() + random.nextDouble(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == getSource() || fluid == getFlowing();
    }

    @Override
    public Fluid getFlowing() {
        return properties.flowing.get();
    }

    @Override
    public Fluid getSource() {
        return properties.still.get();
    }

    @Override
    protected boolean canConvertToSource(net.minecraft.server.level.ServerLevel level) {
        return false;
    }

    /**
     * Perform actions when fluid flows into a replaceable block. Water drops
     * the block's loot table. Lava plays the "block.lava.extinguish" sound.
     */
    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    public Item getBucket() {
        return properties.bucket != null ? properties.bucket.get() : Items.AIR;
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public int getSlopeFindDistance(LevelReader worldView) {
        return properties.slopeFindDistance;
    }

    @Override
    public int getDropOff(LevelReader worldView) {
        return properties.levelDecreasePerBlock;
    }

    @Override
    public int getAmount(FluidState state) {
        return 5;
    }

    @Override
    public int getTickDelay(LevelReader worldView) {
        return properties.tickRate;
    }

    @Override
    protected float getExplosionResistance() {
        return properties.explosionResistance;
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return properties.block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    public static class Flowing extends ChemicalFluid {

        public Flowing(Properties properties) {
            super(properties);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends ChemicalFluid {

        public Still(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }

    public static class Properties
    {
        private final Supplier<? extends Fluid> still;
        private final Supplier<? extends Fluid> flowing;
        private Supplier<? extends Item> bucket;
        private Supplier<? extends LiquidBlock> block;
        private int slopeFindDistance = 4;
        private int levelDecreasePerBlock = 1;
        private float explosionResistance = 100.0F;
        private int tickRate = 5;
        private final int color;

        public Properties(Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing, FluidAttributes attributes)
        {
            this.still = still;
            this.flowing = flowing;
            this.color = attributes.color;
        }

        public Properties bucket(Supplier<? extends Item> bucket)
        {
            this.bucket = bucket;
            return this;
        }

        public Properties block(Supplier<? extends LiquidBlock> block)
        {
            this.block = block;
            return this;
        }

        public Properties slopeFindDistance(int slopeFindDistance)
        {
            this.slopeFindDistance = slopeFindDistance;
            return this;
        }

        public Properties levelDecreasePerBlock(int levelDecreasePerBlock)
        {
            this.levelDecreasePerBlock = levelDecreasePerBlock;
            return this;
        }

        public Properties explosionResistance(float explosionResistance)
        {
            this.explosionResistance = explosionResistance;
            return this;
        }

        public Properties tickRate(int tickRate)
        {
            this.tickRate = tickRate;
            return this;
        }
    }
}
