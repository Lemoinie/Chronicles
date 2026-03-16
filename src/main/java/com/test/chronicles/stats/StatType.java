package com.test.chronicles.stats;

public enum StatType {
    STRENGTH("Strength"), AGILITY("Agility"), INTELLIGENCE("Intelligence"),
    VITALITY("Vitality"), ENDURANCE("Endurance"), LUCK("Luck"),
    DEFENSE("Defense"), ATTACK_SPEED("Attack Speed"), MOVEMENT_SPEED("Movement Speed"),
    MANA("Mana"), MANA_REGEN("Mana Regen"), HEALTH("Health"),
    HEALTH_REGEN("Health Regen"), CRIT_CHANCE("Crit Chance"), CRIT_DAMAGE("Crit Damage");

    private final String displayName;
    StatType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
