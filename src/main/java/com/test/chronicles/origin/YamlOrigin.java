package com.test.chronicles.origin;

import com.test.chronicles.attributes.ModifierType;
import com.test.chronicles.attributes.AttributeModifier;
import com.test.chronicles.attributes.AttributeType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final List<AttributeModifier> modifiers;

    private YamlOrigin(String id, String displayName, String description, List<AttributeModifier> modifiers) {
        this.id = id; this.displayName = displayName;
        this.description = description; this.modifiers = List.copyOf(modifiers);
    }

    public static YamlOrigin fromFile(File file) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String id = cfg.getString("id", file.getName().replace(".yml", ""));
        String name = cfg.getString("display-name", id);
        String desc = cfg.getString("description", "");

        List<AttributeModifier> mods = new ArrayList<>();
        List<Map<?, ?>> list = cfg.getMapList("stat-modifiers");
        for (Map<?, ?> entry : list) {
            AttributeType type = AttributeType.valueOf(entry.get("stat").toString());
            double value  = Double.parseDouble(entry.get("value").toString());
            Object mtObj = entry.get("type");
            ModifierType mt = ModifierType.valueOf(mtObj != null ? mtObj.toString() : "FLAT");
            mods.add(new AttributeModifier("origin:" + id, type, value, mt));
        }
        return new YamlOrigin(id, name, desc, mods);
    }

    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public List<AttributeModifier> getAttributeModifiers() { return modifiers; }
}
