package com.test.chronicles.playerclass;

import com.test.chronicles.attributes.ModifierType;
import com.test.chronicles.attributes.AttributeModifier;
import com.test.chronicles.attributes.AttributeType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data-driven class loaded from YAML.
 *
 * Example (data/classes/warrior.yml):
 * <pre>
 * id: warrior
 * display-name: "Warrior"
 * description: "A strong melee fighter."
 * stat-modifiers:
 *   - stat: STRENGTH
 *     value: 10
 *     type: FLAT
 *   - stat: DEFENSE
 *     value: 0.15
 *     type: PERCENT
 * allowed-skills:
 *   - shield_bash
 *   - iron_skin
 * </pre>
 */
public class YamlPlayerClass implements PlayerClass {

    private final String id, displayName, description;
    private final List<AttributeModifier> modifiers;
    private final List<String> allowedSkills;

    private YamlPlayerClass(String id, String displayName, String description,
                            List<AttributeModifier> modifiers, List<String> allowedSkills) {
        this.id = id; this.displayName = displayName; this.description = description;
        this.modifiers = List.copyOf(modifiers); this.allowedSkills = List.copyOf(allowedSkills);
    }

    public static YamlPlayerClass fromFile(File file) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String id = cfg.getString("id", file.getName().replace(".yml", ""));
        List<AttributeModifier> mods = new ArrayList<>();
        List<Map<?, ?>> list = cfg.getMapList("stat-modifiers");
        for (Map<?, ?> entry : list) {
            AttributeType type = AttributeType.valueOf(entry.get("stat").toString());
            double value  = Double.parseDouble(entry.get("value").toString());
            Object typeObj = entry.get("type");
            ModifierType mt = ModifierType.valueOf(typeObj != null ? typeObj.toString() : "FLAT");
            mods.add(new AttributeModifier("class:" + id, type, value, mt));
        }
        return new YamlPlayerClass(id, cfg.getString("display-name", id),
                cfg.getString("description", ""), mods, cfg.getStringList("allowed-skills"));
    }

    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public List<AttributeModifier> getAttributeModifiers() { return modifiers; }
    @Override public List<String> getAllowedSkills() { return allowedSkills; }
}
