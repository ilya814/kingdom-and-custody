package com.ilya814.kingdomandcustody.block;

import com.ilya814.kingdomandcustody.block.entity.ScannerBlockEntity;
import com.ilya814.kingdomandcustody.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ScannerBlock extends BaseEntityBlock {

    public static final MapCodec<ScannerBlock> CODEC = simpleCodec(ScannerBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public ScannerBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(FACING, Direction.NORTH));
    }

    @Override public MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(POWERED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
            .setValue(FACING, ctx.getNearestLookingDirection().getOpposite())
            .setValue(POWERED, false);
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ScannerBlockEntity(pos, state);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlocks.SCANNER_BLOCK_ENTITY,
            level.isClientSide() ? null : ScannerBlockEntity::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        var be = level.getBlockEntity(pos);
        if (!(be instanceof ScannerBlockEntity scanner)) return InteractionResult.PASS;
        var held = player.getMainHandItem();

        if (held.is(Items.STICK)) {
            if (player.isShiftKeyDown()) {
                scanner.setPos2(player.blockPosition());
                player.displayClientMessage(Component.literal("§6[Scanner] §fCorner 2: " + player.blockPosition().toShortString()), false);
            } else {
                scanner.setPos1(player.blockPosition());
                player.displayClientMessage(Component.literal("§6[Scanner] §fCorner 1: " + player.blockPosition().toShortString()), false);
            }
            scanner.setChanged();
            return InteractionResult.SUCCESS;
        }
        if (held.is(Items.LEVER)) {
            scanner.startLeverPairing(player.getUUID());
            player.displayClientMessage(Component.literal("§6[Scanner] §fNow right-click any placed lever!"), false);
            return InteractionResult.SUCCESS;
        }
        if (held.isEmpty()) { scanner.showInfo(player); return InteractionResult.SUCCESS; }
        return InteractionResult.PASS;
    }

    @Override public boolean isSignalSource(BlockState s) { return true; }
    @Override public int getSignal(BlockState s, BlockGetter l, BlockPos p, Direction d) { return s.getValue(POWERED) ? 15 : 0; }
    @Override public int getDirectSignal(BlockState s, BlockGetter l, BlockPos p, Direction d) { return s.getValue(POWERED) ? 15 : 0; }
}
