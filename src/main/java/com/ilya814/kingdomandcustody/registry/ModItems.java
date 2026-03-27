package com.ilya814.kingdomandcustody.registry;

import com.ilya814.kingdomandcustody.KingdomAndCustody;
import com.ilya814.kingdomandcustody.armor.ArmorTier;
import com.ilya814.kingdomandcustody.armor.ModArmorMaterials;
import com.ilya814.kingdomandcustody.item.KeyItem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import java.util.EnumMap;
import java.util.Map;

public class ModItems {

    public static final Map<ArmorTier, Map<ArmorType, Item>> ARMOR = new EnumMap<>(ArmorTier.class);

    // Ingots / fragments
    public static final Item ROYAL_INGOT    = register("royal_ingot",    new Item(new Item.Properties()));
    public static final Item NOBLE_INGOT    = register("noble_ingot",    new Item(new Item.Properties()));
    public static final Item DIVINE_INGOT   = register("divine_ingot",   new Item(new Item.Properties()));
    public static final Item VOID_SHARD     = register("void_shard",     new Item(new Item.Properties()));
    public static final Item CELESTIAL_FRAG = register("celestial_fragment", new Item(new Item.Properties()));

    // Block items for ores
    public static final Item ROYAL_ORE_ITEM    = registerBlock("royal_ore");
    public static final Item NOBLE_ORE_ITEM    = registerBlock("noble_ore");
    public static final Item DIVINE_ORE_ITEM   = registerBlock("divine_ore");
    public static final Item VOID_ORE_ITEM     = registerBlock("void_ore");
    public static final Item CELESTIAL_ORE_ITEM = registerBlock("celestial_ore");

    // Scanner block item
    public static final Item SCANNER_BLOCK_ITEM = registerBlock("scanner_block");

    // 20 Keys
    public static final KeyItem[] KEYS = new KeyItem[20];

    public static void register() {
        // Register armor
        for (ArmorTier tier : ArmorTier.values()) {
            Holder<ArmorMaterial> material = ModArmorMaterials.MATERIALS.get(tier);
            Map<ArmorType, Item> pieces = new EnumMap<>(ArmorType.class);
            for (ArmorType type : ArmorType.values()) {
                String name = tier.id + "_" + type.getName();
                Item item = Registry.register(BuiltInRegistries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name),
                    new ArmorItem(material, type, new Item.Properties().stacksTo(1)));
                pieces.put(type, item);
            }
            ARMOR.put(tier, pieces);
        }

        // Register 20 keys
        for (int i = 1; i <= 20; i++) {
            KeyItem key = new KeyItem(i);
            KEYS[i - 1] = (KeyItem) Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, "key_" + i),
                key);
        }
    }

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name), item);
    }

    private static Item registerBlock(String name) {
        Block block = BuiltInRegistries.BLOCK.get(
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name)).orElseThrow().value();
        return register(name, new BlockItem(block, new Item.Properties()));
    }
}
