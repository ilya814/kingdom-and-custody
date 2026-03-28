package com.ilya814.kingdomandcustody.item;

import com.ilya814.kingdomandcustody.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class KeyItem extends Item {
    private final int keyNumber;

    public KeyItem(int keyNumber) {
        super(new Item.Properties().stacksTo(1));
        this.keyNumber = keyNumber;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos().relative(ctx.getClickedFace());
        if (!level.isClientSide() && level.isEmptyBlock(pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.KEY_SIGNAL_BLOCK.defaultBlockState());
            ctx.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public int getKeyNumber() { return keyNumber; }
}
