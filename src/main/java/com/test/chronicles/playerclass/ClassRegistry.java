package com.test.chronicles.playerclass;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ClassRegistry {
    private final Map<String, PlayerClass> classes = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public void loadFromFolder(File folder) {
        if (!folder.exists()) { folder.mkdirs(); return; }
        File[] files = folder.listFiles((d, n) -> n.endsWith(".yml"));
        if (files == null) return;
        for (File f : files) {
            try {
                PlayerClass c = YamlPlayerClass.fromFile(f);
                classes.put(c.getId(), c);
                LOG.info("[Classes] Loaded: " + c.getId());
            } catch (Exception ex) { LOG.warning("[Classes] Failed: " + f.getName() + ": " + ex.getMessage()); }
        }
    }

    public void register(PlayerClass c) { classes.put(c.getId(), c); }
    public PlayerClass get(String id) { return classes.get(id); }
    public Collection<PlayerClass> getAll() { return Collections.unmodifiableCollection(classes.values()); }
}
