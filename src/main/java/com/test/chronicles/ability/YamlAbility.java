package com.test.chronicles.ability;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlAbility implements Ability {

    private final String id, displayName, description, requiredClass, requiredSkill;
    private final int cooldownSeconds, manaCost;

    private YamlAbility(String id, String displayName, String description,
                        int cooldownSeconds, int manaCost, String requiredClass, String requiredSkill) {
        this.id = id; this.displayName = displayName; this.description = description;
        this.cooldownSeconds = cooldownSeconds; this.manaCost = manaCost;
        this.requiredClass = requiredClass; this.requiredSkill = requiredSkill;
    }

    public static YamlAbility fromFile(File file) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String id = cfg.getString("id", file.getName().replace(".yml", ""));
        return new YamlAbility(id, cfg.getString("display-name", id),
                cfg.getString("description", ""), cfg.getInt("cooldown-seconds", 0),
                cfg.getInt("mana-cost", 0), cfg.getString("required-class"),
                cfg.getString("required-skill"));
    }

    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getCooldownSeconds() { return cooldownSeconds; }
    @Override public int getManaCost() { return manaCost; }
    @Override public String getRequiredClass() { return requiredClass; }
    @Override public String getRequiredSkill() { return requiredSkill; }
}
