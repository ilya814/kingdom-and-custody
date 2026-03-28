package com.ilya814.kingdomandcustody.armor;

public enum ArmorTier {
    KING            (1,  "king",           11, 16, 13, 11, 3.5f, 0.1f),
    HIGH_KING       (2,  "high_king",      12, 17, 14, 12, 3.6f, 0.11f),
    OVERLORD        (3,  "overlord",       12, 18, 15, 12, 3.7f, 0.12f),
    GRAND_DUKE      (4,  "grand_duke",     13, 19, 15, 13, 3.8f, 0.13f),
    ARCHDUKE        (5,  "archduke",       13, 20, 16, 13, 3.9f, 0.14f),
    PRINCE_ROYAL    (6,  "prince_royal",   14, 21, 17, 14, 4.0f, 0.15f),
    WARLORD         (7,  "warlord",        14, 22, 17, 14, 4.1f, 0.16f),
    CHAMPION        (8,  "champion",       15, 23, 18, 15, 4.2f, 0.17f),
    CONQUEROR       (9,  "conqueror",      15, 24, 19, 15, 4.3f, 0.18f),
    SOVEREIGN       (10, "sovereign",      16, 25, 19, 16, 4.4f, 0.19f),
    EMPEROR         (11, "emperor",        16, 26, 20, 16, 4.5f, 0.20f),
    HIGH_EMPEROR    (12, "high_emperor",   17, 27, 21, 17, 4.6f, 0.21f),
    GRAND_EMPEROR   (13, "grand_emperor",  17, 28, 21, 17, 4.7f, 0.22f),
    SUPREME_RULER   (14, "supreme_ruler",  18, 29, 22, 18, 4.8f, 0.23f),
    CELESTIAL_KING  (15, "celestial_king", 18, 30, 23, 18, 4.9f, 0.24f),
    DIVINE_RULER    (16, "divine_ruler",   19, 31, 23, 19, 5.0f, 0.25f),
    DEMIGOD         (17, "demigod",        19, 32, 24, 19, 5.1f, 0.26f),
    ANCIENT_GOD     (18, "ancient_god",    20, 33, 25, 20, 5.2f, 0.27f),
    SUPREME_GOD     (19, "supreme_god",    20, 34, 25, 20, 5.3f, 0.28f),
    ETERNAL_GOD     (20, "eternal_god",    21, 35, 26, 21, 5.4f, 0.29f),
    IMMORTAL        (21, "immortal",       30, 50, 40, 30, 10.0f, 1.0f);

    public final int tier;
    public final String id;
    public final int helmetDef, chestDef, legsDef, bootsDef;
    public final float toughness, kbRes;

    ArmorTier(int tier, String id, int boots, int chest, int legs, int helmet,
              float toughness, float kbRes) {
        this.tier = tier; this.id = id;
        this.helmetDef = helmet; this.chestDef = chest;
        this.legsDef = legs; this.bootsDef = boots;
        this.toughness = toughness; this.kbRes = kbRes;
    }
}
