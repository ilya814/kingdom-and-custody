package com.ilya814.kingdomandcustody.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class KeySignalBlock extends Block {
    public static final IntegerProperty TICKS_LEFT = IntegerProperty.create("ticks_left", 0, 9);

    public KeySignalBlock(BlockBehaviour.Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(TICKS_LEFT, 9));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TICKS_LEFT);
    }

    @Override
    public void onPlace(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 20); // tick every second
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int left = state.getValue(TICKS_LEFT);
        if (left <= 0) {
            level.removeBlock(pos, false);
            level.updateNeighborsAt(pos, this);
        } else {
            level.setBlock(pos, state.setValue(TICKS_LEFT, left - 1), 3);
            level.updateNeighborsAt(pos, this);
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) { return true; }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return state.getValue(TICKS_LEFT) > 0 ? 15 : 0;
    }
}
