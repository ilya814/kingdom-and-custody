package com.ilya814.kingdomandcustody.armor;

import com.ilya814.kingdomandcustody.KingdomAndCustody;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.ArmorMaterial;
import java.util.EnumMap;
import java.util.Map;

public class ModArmorMaterials {

    public static final Map<ArmorTier, ResourceKey<ArmorMaterial>> KEYS = new EnumMap<>(ArmorTier.class);

    public static void register() {
        for (ArmorTier tier : ArmorTier.values()) {
            KEYS.put(tier, ResourceKey.create(
                Registries.ARMOR_MATERIAL,
                ResourceLocation.fromNamespaceAndPath(KingdomAndCustody.MOD_ID, tier.id)
            ));
        }
    }
}
