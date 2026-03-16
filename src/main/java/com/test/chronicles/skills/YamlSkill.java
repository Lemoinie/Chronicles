package com.test.chronicles.skills;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class YamlSkill implements Skill {

    private final String id, displayName, description;
    private final int maxLevel;
    private final List<String> prerequisites, allowedClasses;

    private YamlSkill(String id, String displayName, String description,
                      int maxLevel, List<String> prerequisites, List<String> allowedClasses) {
        this.id = id; this.displayName = displayName; this.description = description;
        this.maxLevel = maxLevel;
        this.prerequisites = List.copyOf(prerequisites);
        this.allowedClasses = List.copyOf(allowedClasses);
    }

    public static YamlSkill fromFile(File file) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String id = cfg.getString("id", file.getName().replace(".yml", ""));
        return new YamlSkill(id, cfg.getString("display-name", id),
                cfg.getString("description", ""), cfg.getInt("max-level", 1),
                cfg.getStringList("prerequisites"), cfg.getStringList("allowed-classes"));
    }

    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getMaxLevel() { return maxLevel; }
    @Override public List<String> getPrerequisites() { return prerequisites; }
    @Override public List<String> getAllowedClasses() { return allowedClasses; }
}
