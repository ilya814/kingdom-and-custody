package com.ilya814.kingdomandcustody.mixin;

import com.ilya814.kingdomandcustody.block.entity.ScannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeverBlock.class)
public class LeverMixin {

    @Inject(method = "useWithoutItem", at = @At("HEAD"))
    private void onUse(BlockState state, Level level, BlockPos pos, Player player,
                       BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide()) return;
        BlockPos scannerPos = ScannerBlockEntity.PENDING_PAIRS.remove(player.getUUID());
        if (scannerPos == null) return;
        var be = level.getBlockEntity(scannerPos);
        if (be instanceof ScannerBlockEntity scanner) {
            scanner.setPairedLever(pos);
            player.displayClientMessage(Component.literal(
                "§6[Scanner] §fLever at " + pos.toShortString() + " paired!"), false);
        }
    }
}
