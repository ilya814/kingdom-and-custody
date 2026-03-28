package com.ilya814.kingdomandcustody.registry;

import com.ilya814.kingdomandcustody.KingdomAndCustody;
import com.ilya814.kingdomandcustody.armor.ArmorTier;
import com.ilya814.kingdomandcustody.armor.ModArmorMaterials;
import com.ilya814.kingdomandcustody.item.KeyItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import java.util.EnumMap;
import java.util.Map;

public class ModItems {

    public static final Map<ArmorTier, Map<ArmorType, Item>> ARMOR = new EnumMap<>(ArmorTier.class);

    public static final Item ROYAL_INGOT    = reg("royal_ingot",    new Item(new Item.Properties()));
    public static final Item NOBLE_INGOT    = reg("noble_ingot",    new Item(new Item.Properties()));
    public static final Item DIVINE_INGOT   = reg("divine_ingot",   new Item(new Item.Properties()));
    public static final Item VOID_SHARD     = reg("void_shard",     new Item(new Item.Properties()));
    public static final Item CELESTIAL_FRAG = reg("celestial_fragment", new Item(new Item.Properties()));

    public static final KeyItem[] KEYS = new KeyItem[20];

    public static void register() {
        for (ArmorTier tier : ArmorTier.values()) {
            ResourceKey<ArmorMaterial> material = ModArmorMaterials.KEYS.get(tier);
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

        for (int i = 1; i <= 20; i++) {
            KeyItem key = new KeyItem(i);
            KEYS[i - 1] = (KeyItem) Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, "key_" + i), key);
        }

        // Ore block items
        for (String name : new String[]{"royal_ore","noble_ore","divine_ore","void_ore","celestial_ore","scanner_block"}) {
            var block = BuiltInRegistries.BLOCK.get(
                ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name));
            block.ifPresent(b -> reg(name, new BlockItem(b.value(), new Item.Properties())));
        }
    }

    public static Item reg(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name), item);
    }
}
