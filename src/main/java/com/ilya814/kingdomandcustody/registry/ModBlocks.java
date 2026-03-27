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

    public static final Block SCANNER_BLOCK = register("scanner_block",
        new ScannerBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).strength(3.5f)
            .sound(SoundType.METAL).requiresCorrectToolForDrops()));

    public static final Block KEY_SIGNAL_BLOCK = register("key_signal_block",
        new KeySignalBlock(BlockBehaviour.Properties.of()
            .strength(0f).noCollission().noLootTable()
            .lightLevel(s -> 5)));

    public static final Block ROYAL_ORE = register("royal_ore",
        new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD)
            .strength(3f, 3f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block NOBLE_ORE = register("noble_ore",
        new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
            .strength(4f, 4f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block DIVINE_ORE = register("divine_ore",
        new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
            .strength(5f, 5f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block VOID_ORE = register("void_ore",
        new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
            .strength(6f, 6f).sound(SoundType.NETHER_ORE).requiresCorrectToolForDrops()));

    public static final Block CELESTIAL_ORE = register("celestial_ore",
        new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
            .strength(7f, 7f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static BlockEntityType<ScannerBlockEntity> SCANNER_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        SCANNER_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, "scanner_block"),
            BlockEntityType.Builder.of(ScannerBlockEntity::new, SCANNER_BLOCK).build()
        );
    }

    private static Block register(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name), block);
    }

    public static void register() {}
}
