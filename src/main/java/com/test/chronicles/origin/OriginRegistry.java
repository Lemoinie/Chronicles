package com.test.chronicles.origin;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OriginRegistry {

    private final Map<String, Origin> origins = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public void loadFromFolder(File folder) {
        if (!folder.exists()) { folder.mkdirs(); return; }
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;
        for (File f : files) {
            try {
                Origin o = YamlOrigin.fromFile(f);
                origins.put(o.getId(), o);
                LOG.info("[Origins] Loaded: " + o.getId());
            } catch (Exception ex) {
                LOG.warning("[Origins] Failed to load " + f.getName() + ": " + ex.getMessage());
            }
        }
    }

    public void register(Origin origin) { origins.put(origin.getId(), origin); }
    public Origin get(String id) { return origins.get(id); }
    public Collection<Origin> getAll() { return Collections.unmodifiableCollection(origins.values()); }
}
