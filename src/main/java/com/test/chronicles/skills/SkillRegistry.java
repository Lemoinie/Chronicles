package com.test.chronicles.skills;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SkillRegistry {

    private final Map<String, Skill> skills = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public void loadFromFolder(File folder) {
        if (!folder.exists()) { folder.mkdirs(); return; }
        File[] files = folder.listFiles((d, n) -> n.endsWith(".yml"));
        if (files == null) return;
        for (File f : files) {
            try {
                Skill s = YamlSkill.fromFile(f);
                skills.put(s.getId(), s);
                LOG.info("[Skills] Loaded: " + s.getId());
            } catch (Exception ex) { LOG.warning("[Skills] Failed: " + f.getName() + ": " + ex.getMessage()); }
        }
    }

    public void register(Skill skill) { skills.put(skill.getId(), skill); }
    public Skill get(String id) { return skills.get(id); }
    public Collection<Skill> getAll() { return Collections.unmodifiableCollection(skills.values()); }
}
