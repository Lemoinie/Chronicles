package com.test.chronicles.origin;

import com.test.chronicles.stats.ModifierType;
import com.test.chronicles.stats.StatModifier;
import com.test.chronicles.stats.StatType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-driven origin loaded from a YAML file.
 *
 * Example YAML (data/origins/human.yml):
 * <pre>
 * id: human
 * display-name: "Human"
 * description: "Adaptable and resilient."
 * stat-modifiers:
 *   - stat: LUCK
 *     value: 0.05
 *     type: PERCENT
 *   - stat: STRENGTH
 *     value: 2
 *     type: FLAT
 * </pre>
 */
public class YamlOrigin implements Origin {

    private final String id, displayName, description;
    private final List<StatModifier> modifiers;

    private YamlOrigin(String id, String displayName, String description, List<StatModifier> modifiers) {
        this.id = id; this.displayName = displayName;
        this.description = description; this.modifiers = List.copyOf(modifiers);
    }

    public static YamlOrigin fromFile(File file) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String id = cfg.getString("id", file.getName().replace(".yml", ""));
        String name = cfg.getString("display-name", id);
        String desc = cfg.getString("description", "");

        List<StatModifier> mods = new ArrayList<>();
        var list = cfg.getMapList("stat-modifiers");
        for (var entry : list) {
            StatType type = StatType.valueOf(entry.get("stat").toString());
            double value  = Double.parseDouble(entry.get("value").toString());
            ModifierType mt = ModifierType.valueOf(entry.getOrDefault("type", "FLAT").toString());
            mods.add(new StatModifier("origin:" + id, type, value, mt));
        }
        return new YamlOrigin(id, name, desc, mods);
    }

    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public List<StatModifier> getStatModifiers() { return modifiers; }
}
