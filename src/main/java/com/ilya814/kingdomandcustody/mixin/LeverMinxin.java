package com.ilya814.kingdomandcustody.mixin;

import com.ilya814.kingdomandcustody.block.entity.ScannerBlockEntity;
import com.ilya814.kingdomandcustody.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Block;

@Mixin(LeverBlock.class)
public class LeverMixin {

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = false)
    private void onUse(BlockState state, Level level, BlockPos pos, Player player,
                       BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide) return;

        BlockPos scannerPos = ScannerBlockEntity.PENDING_PAIRS.remove(player.getUUID());
        if (scannerPos == null) return;

        var be = level.getBlockEntity(scannerPos);
        if (be instanceof ScannerBlockEntity scanner) {
            scanner.setPairedLever(pos);
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "§6[Scanner] §fLever paired! Scanner at " + scannerPos.toShortString() +
                " linked to lever at " + pos.toShortString()));
        }
    }
}
