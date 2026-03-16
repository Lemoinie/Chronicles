package com.test.chronicles.attributes;

public enum AttributeType {
    // --- Primary Attributes ---
    STRENGTH("Strength", "STR"),
    AGILITY("Agility", "AGI"),
    VITALITY("Vitality", "VIT"),
    INTELLIGENCE("Intelligence", "INT"),
    WISDOM("Wisdom", "WIS"),
    LUCK("Luck", "LUK"),
    ENDURANCE("Endurance", "END"),

    // --- Base Secondary Stats ---
    HEALTH("Health"),
    HEALTH_REGEN("Health Regen"),
    MANA("Mana"),
    MANA_REGEN("Mana Regen"),
    MOVEMENT_SPEED("Movement Speed"),
    ATTACK_SPEED("Attack Speed"),
    CRIT_CHANCE("Crit Chance"),
    CRIT_DAMAGE("Crit Damage"),
    DEFENSE("Defense"),
    MAGIC_DEFENSE("Magic Defense"),
    MAGIC_RESISTANCE("Magic Resistance"),
    EXTRA_HIT_BONUS("Extra Hit Bonus"),
    DODGE("Dodge"),
    XP_BONUS("XP Bonus"),
    ANVIL_DISCOUNT("Anvil Discount");

    private final String displayName;
    private final String shorthand;

    AttributeType(String displayName) {
        this(displayName, displayName);
    }

    AttributeType(String displayName, String shorthand) {
        this.displayName = displayName;
        this.shorthand = shorthand;
    }

    public String getDisplayName() { return displayName; }
    public String getShorthand() { return shorthand; }
}
