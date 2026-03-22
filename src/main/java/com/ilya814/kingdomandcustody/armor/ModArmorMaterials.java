package com.ilya814.kingdomandcustody.armor;

import com.ilya814.kingdomandcustody.KingdomAndCustody;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModArmorMaterials {

    public static final Map<ArmorTier, Holder<ArmorMaterial>> MATERIALS = new EnumMap<>(ArmorTier.class);

    public static void register() {
        for (ArmorTier tier : ArmorTier.values()) {
            Map<ArmorType, Integer> protection = new EnumMap<>(ArmorType.class);
            protection.put(ArmorType.BOOTS, tier.bootsProtection);
            protection.put(ArmorType.CHESTPLATE, tier.chestplateProtection);
            protection.put(ArmorType.LEGGINGS, tier.leggingsProtection);
            protection.put(ArmorType.HELMET, tier.helmetProtection);

            ArmorMaterial material = new ArmorMaterial(
                protection,
                30,
                SoundEvents.ARMOR_EQUIP_NETHERITE,
                () -> Ingredient.EMPTY,
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, tier.id))),
                tier.toughness,
                tier.knockbackResistance
            );

            Holder<ArmorMaterial> holder = Registry.registerForHolder(
                BuiltInRegistries.ARMOR_MATERIAL,
                ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, tier.id),
                material
            );
            MATERIALS.put(tier, holder);
        }
    }
}
