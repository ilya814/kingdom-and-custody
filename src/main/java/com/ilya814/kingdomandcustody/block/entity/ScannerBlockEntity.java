package com.ilya814.kingdomandcustody.block.entity;

import com.ilya814.kingdomandcustody.block.ScannerBlock;
import com.ilya814.kingdomandcustody.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.ValueInput;
import net.minecraft.util.ValueOutput;
import java.util.*;

public class ScannerBlockEntity extends BlockEntity {
    private BlockPos pos1 = null, pos2 = null, pairedLeverPos = null;
    private boolean powered = false;
    private int tickCounter = 0;

    public static final Map<UUID, BlockPos> PENDING_PAIRS = new HashMap<>();

    public ScannerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SCANNER_BLOCK_ENTITY, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ScannerBlockEntity be) {
        if (level.isClientSide()) return;
        if (++be.tickCounter % 20 != 0) return;
        be.tickCounter = 0;

        if (be.pairedLeverPos != null) {
            var ls = level.getBlockState(be.pairedLeverPos);
            if (ls.getBlock() instanceof LeverBlock && ls.getValue(BlockStateProperties.POWERED)) {
                if (be.powered) {
                    be.powered = false;
                    level.setBlockAndUpdate(pos, state.setValue(ScannerBlock.POWERED, false));
                    level.updateNeighborsAt(pos, state.getBlock());
                    be.setChanged();
                }
                return;
            }
        }

        boolean should = false;
        if (be.pos1 != null && be.pos2 != null) {
            AABB area = new AABB(
                Math.min(be.pos1.getX(), be.pos2.getX()), Math.min(be.pos1.getY(), be.pos2.getY()), Math.min(be.pos1.getZ(), be.pos2.getZ()),
                Math.max(be.pos1.getX(), be.pos2.getX()) + 1, Math.max(be.pos1.getY(), be.pos2.getY()) + 1, Math.max(be.pos1.getZ(), be.pos2.getZ()) + 1
            );
            for (Player p : level.getEntitiesOfClass(Player.class, area))
                if (isEmpty(p)) { should = true; break; }
        }

        if (should != be.powered) {
            be.powered = should;
            level.setBlockAndUpdate(pos, state.setValue(ScannerBlock.POWERED, should));
            level.updateNeighborsAt(pos, state.getBlock());
            be.setChanged();
        }
    }

    private static boolean isEmpty(Player p) {
        Inventory inv = p.getInventory();
        for (int i = 0; i <= 40; i++) if (!inv.getItem(i).isEmpty()) return false;
        return true;
    }

    public void setPos1(BlockPos p) { this.pos1 = p; }
    public void setPos2(BlockPos p) { this.pos2 = p; }
    public void startLeverPairing(UUID uuid) { PENDING_PAIRS.put(uuid, worldPosition); }
    public void setPairedLever(BlockPos p) { this.pairedLeverPos = p; setChanged(); }

    public void showInfo(Player player) {
        player.displayClientMessage(Component.literal("§6--- Scanner Info ---"), false);
        player.displayClientMessage(Component.literal("§fCorner 1: " + (pos1 != null ? pos1.toShortString() : "§cnot set")), false);
        player.displayClientMessage(Component.literal("§fCorner 2: " + (pos2 != null ? pos2.toShortString() : "§cnot set")), false);
        player.displayClientMessage(Component.literal("§fLever: " + (pairedLeverPos != null ? pairedLeverPos.toShortString() : "§cnone")), false);
        player.displayClientMessage(Component.literal("§fSignal: " + (powered ? "§aPOWERED" : "§cOFF")), false);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (pos1 != null) { output.putInt("p1x", pos1.getX()); output.putInt("p1y", pos1.getY()); output.putInt("p1z", pos1.getZ()); }
        if (pos2 != null) { output.putInt("p2x", pos2.getX()); output.putInt("p2y", pos2.getY()); output.putInt("p2z", pos2.getZ()); }
        if (pairedLeverPos != null) { output.putInt("lx", pairedLeverPos.getX()); output.putInt("ly", pairedLeverPos.getY()); output.putInt("lz", pairedLeverPos.getZ()); }
        output.putBoolean("powered", powered);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        Optional<Integer> p1x = input.getInt("p1x");
        if (p1x.isPresent()) pos1 = new BlockPos(p1x.get(), input.getInt("p1y").orElse(0), input.getInt("p1z").orElse(0));
        Optional<Integer> p2x = input.getInt("p2x");
        if (p2x.isPresent()) pos2 = new BlockPos(p2x.get(), input.getInt("p2y").orElse(0), input.getInt("p2z").orElse(0));
        Optional<Integer> lx = input.getInt("lx");
        if (lx.isPresent()) pairedLeverPos = new BlockPos(lx.get(), input.getInt("ly").orElse(0), input.getInt("lz").orElse(0));
        powered = input.getBoolean("powered").orElse(false);
    }
}
