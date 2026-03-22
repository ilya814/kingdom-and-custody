package com.ilya814.kingdomandcustody.registry;

import com.ilya814.kingdomandcustody.KingdomAndCustody;
import com.ilya814.kingdomandcustody.armor.ArmorTier;
import com.ilya814.kingdomandcustody.armor.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;
import java.util.Map;

public class ModItems {

    public static final Map<ArmorTier, Map<ArmorType, Item>> ARMOR = new EnumMap<>(ArmorTier.class);

    public static void register() {
        for (ArmorTier tier : ArmorTier.values()) {
            Holder<ArmorMaterial> material = ModArmorMaterials.MATERIALS.get(tier);
            Map<ArmorType, Item> pieces = new EnumMap<>(ArmorType.class);

            for (ArmorType type : ArmorType.values()) {
                String name = tier.id + "_" + type.getName();
                Item item = Registry.register(
                    BuiltInRegistries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, name),
                    new ArmorItem(material, type, new Item.Properties().stacksTo(1))
                );
                pieces.put(type, item);
            }

            ARMOR.put(tier, pieces);
        }
    }
}
