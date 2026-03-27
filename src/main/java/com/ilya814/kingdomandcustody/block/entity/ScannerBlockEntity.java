package com.ilya814.kingdomandcustody.block.entity;

import com.ilya814.kingdomandcustody.block.ScannerBlock;
import com.ilya814.kingdomandcustody.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import java.util.*;

public class ScannerBlockEntity extends BlockEntity {
    private BlockPos pos1 = null;
    private BlockPos pos2 = null;
    private BlockPos pairedLeverPos = null;
    private boolean powered = false;
    private int tickCounter = 0;

    public static final Map<UUID, BlockPos> PENDING_PAIRS = new HashMap<>();

    public ScannerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SCANNER_BLOCK_ENTITY, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ScannerBlockEntity be) {
        if (level.isClientSide) return;
        be.tickCounter++;
        if (be.tickCounter % 20 != 0) return;
        be.tickCounter = 0;

        // Check paired lever - if ON, turn signal off
        if (be.pairedLeverPos != null) {
            var leverState = level.getBlockState(be.pairedLeverPos);
            if (leverState.getBlock() instanceof LeverBlock &&
                leverState.getValue(BlockStateProperties.POWERED)) {
                if (be.powered) {
                    be.powered = false;
                    level.setBlockAndUpdate(pos, state.setValue(ScannerBlock.POWERED, false));
                    level.updateNeighborsAt(pos, state.getBlock());
                    be.setChanged();
                }
                return;
            }
        }

        boolean shouldBePowered = false;
        if (be.pos1 != null && be.pos2 != null) {
            AABB area = new AABB(
                Math.min(be.pos1.getX(), be.pos2.getX()),
                Math.min(be.pos1.getY(), be.pos2.getY()),
                Math.min(be.pos1.getZ(), be.pos2.getZ()),
                Math.max(be.pos1.getX(), be.pos2.getX()) + 1,
                Math.max(be.pos1.getY(), be.pos2.getY()) + 1,
                Math.max(be.pos1.getZ(), be.pos2.getZ()) + 1
            );
            for (Player player : level.getEntitiesOfClass(Player.class, area)) {
                if (hasEmptyInventory(player)) { shouldBePowered = true; break; }
            }
        }

        if (shouldBePowered != be.powered) {
            be.powered = shouldBePowered;
            level.setBlockAndUpdate(pos, state.setValue(ScannerBlock.POWERED, shouldBePowered));
            level.updateNeighborsAt(pos, state.getBlock());
            be.setChanged();
        }
    }

    private static boolean hasEmptyInventory(Player player) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < 36; i++) { if (!inv.getItem(i).isEmpty()) return false; }
        for (int i = 36; i <= 40; i++) { if (!inv.getItem(i).isEmpty()) return false; }
        return true;
    }

    public void setPos1(BlockPos p) { this.pos1 = p; }
    public void setPos2(BlockPos p) { this.pos2 = p; }
    public void startLeverPairing(UUID uuid) { PENDING_PAIRS.put(uuid, worldPosition); }
    public void setPairedLever(BlockPos p) { this.pairedLeverPos = p; setChanged(); }

    public void showInfo(Player player) {
        player.sendSystemMessage(Component.literal("§6--- Scanner Info ---"));
        player.sendSystemMessage(Component.literal("§fCorner 1: " + (pos1 != null ? pos1.toShortString() : "§cnot set")));
        player.sendSystemMessage(Component.literal("§fCorner 2: " + (pos2 != null ? pos2.toShortString() : "§cnot set")));
        player.sendSystemMessage(Component.literal("§fPaired lever: " + (pairedLeverPos != null ? pairedLeverPos.toShortString() : "§cnone")));
        player.sendSystemMessage(Component.literal("§fSignal: " + (powered ? "§aPOWERED (15)" : "§cOFF")));
        player.sendSystemMessage(Component.literal("§7Hold stick + right-click = set corner 1"));
        player.sendSystemMessage(Component.literal("§7Sneak + stick + right-click = set corner 2"));
        player.sendSystemMessage(Component.literal("§7Hold lever + right-click = start lever pairing"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.saveAdditional(tag, reg);
        if (pos1 != null) { tag.putInt("p1x",pos1.getX()); tag.putInt("p1y",pos1.getY()); tag.putInt("p1z",pos1.getZ()); }
        if (pos2 != null) { tag.putInt("p2x",pos2.getX()); tag.putInt("p2y",pos2.getY()); tag.putInt("p2z",pos2.getZ()); }
        if (pairedLeverPos != null) { tag.putInt("lx",pairedLeverPos.getX()); tag.putInt("ly",pairedLeverPos.getY()); tag.putInt("lz",pairedLeverPos.getZ()); }
        tag.putBoolean("powered", powered);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.loadAdditional(tag, reg);
        if (tag.contains("p1x")) pos1 = new BlockPos(tag.getInt("p1x"),tag.getInt("p1y"),tag.getInt("p1z"));
        if (tag.contains("p2x")) pos2 = new BlockPos(tag.getInt("p2x"),tag.getInt("p2y"),tag.getInt("p2z"));
        if (tag.contains("lx")) pairedLeverPos = new BlockPos(tag.getInt("lx"),tag.getInt("ly"),tag.getInt("lz"));
        powered = tag.getBoolean("powered");
    }
}
