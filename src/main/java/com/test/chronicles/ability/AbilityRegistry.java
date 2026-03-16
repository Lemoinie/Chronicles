package com.test.chronicles.ability;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AbilityRegistry {
    private final Map<String, Ability> abilities = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public void loadFromFolder(File folder) {
        if (!folder.exists()) { folder.mkdirs(); return; }
        File[] files = folder.listFiles((d, n) -> n.endsWith(".yml"));
        if (files == null) return;
        for (File f : files) {
            try { Ability a = YamlAbility.fromFile(f); abilities.put(a.getId(), a); LOG.info("[Abilities] Loaded: " + a.getId()); }
            catch (Exception ex) { LOG.warning("[Abilities] Failed: " + f.getName() + ": " + ex.getMessage()); }
        }
    }

    public void register(Ability ability) { abilities.put(ability.getId(), ability); }
    public Ability get(String id) { return abilities.get(id); }
    public Collection<Ability> getAll() { return Collections.unmodifiableCollection(abilities.values()); }
}
