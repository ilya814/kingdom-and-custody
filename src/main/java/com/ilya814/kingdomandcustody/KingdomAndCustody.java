package com.ilya814.kingdomandcustody;

import com.ilya814.kingdomandcustody.armor.ModArmorMaterials;
import com.ilya814.kingdomandcustody.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KingdomAndCustody implements ModInitializer {

    public static final String MOD_ID = "kingdomandcustody";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Kingdom & Custody loading...");
        ModArmorMaterials.register();
        ModItems.register();
        LOGGER.info("Kingdom & Custody loaded! Long live the King.");
    }
}
