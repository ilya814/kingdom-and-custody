package com.ilya814.kingdomandcustody.registry;

import com.ilya814.kingdomandcustody.KingdomAndCustody;
import com.ilya814.kingdomandcustody.block.ScannerBlock;
import com.ilya814.kingdomandcustody.block.KeySignalBlock;
import com.ilya814.kingdomandcustody.block.entity.ScannerBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

    public static final Block SCANNER_BLOCK = reg("scanner_block",
        new ScannerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
            .strength(3.5f).sound(SoundType.METAL).requiresCorrectToolForDrops()));

    public static final Block KEY_SIGNAL_BLOCK = reg("key_signal_block",
        new KeySignalBlock(BlockBehaviour.Properties.of()
            .strength(0f).noCollission().noLootTable().lightLevel(s -> 5)));

    public static final Block ROYAL_ORE    = reg("royal_ore",    ore(MapColor.GOLD, 3f));
    public static final Block NOBLE_ORE    = reg("noble_ore",    ore(MapColor.COLOR_PURPLE, 4f));
    public static final Block DIVINE_ORE   = reg("divine_ore",   ore(MapColor.COLOR_CYAN, 5f));
    public static final Block VOID_ORE     = reg("void_ore",     ore(MapColor.COLOR_BLACK, 6f));
    public static final Block CELESTIAL_ORE = reg("celestial_ore", ore(MapColor.COLOR_YELLOW, 7f));

    public static BlockEntityType<ScannerBlockEntity> SCANNER_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        SCANNER_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, "scanner_block"),
            BlockEntityType.Builder.of(ScannerBlockEntity::new, SCANNER_BLOCK).build()
        );
    }

    private static Block ore(MapColor color, float hardness) {
        return new Block(BlockBehaviour.Properties.of().mapColor(color)
            .strength(hardness, hardness).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }

    private static Block reg(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name), block);
    }

    public static void register() {}
}
