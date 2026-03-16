package com.test.chronicles.job;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class YamlJob implements Job {

    private final String id, displayName, description;
    private final int maxLevel;
    private final long[] levelXp; // levelXp[i] = XP needed to reach level (i+1)

    private YamlJob(String id, String displayName, String description, int maxLevel, long[] levelXp) {
        this.id = id; this.displayName = displayName; this.description = description;
        this.maxLevel = maxLevel; this.levelXp = levelXp;
    }

    public static YamlJob fromFile(File file) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String id = cfg.getString("id", file.getName().replace(".yml", ""));
        List<?> xpList = cfg.getList("level-xp", List.of());
        long[] xp = xpList.stream().mapToLong(o -> Long.parseLong(o.toString())).toArray();
        return new YamlJob(id, cfg.getString("display-name", id),
                cfg.getString("description", ""), cfg.getInt("max-level", xp.length), xp);
    }

    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getMaxLevel() { return maxLevel; }

    @Override
    public long getXpForLevel(int level) {
        int idx = level - 1;
        if (idx < 0 || idx >= levelXp.length) return Long.MAX_VALUE;
        return levelXp[idx];
    }
}
