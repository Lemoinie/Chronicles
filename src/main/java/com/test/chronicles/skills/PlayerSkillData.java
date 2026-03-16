package com.test.chronicles.skills;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Per-player skill progression data. Owned by SkillModule. */
public class PlayerSkillData {

    /** skillId → current level (0 = not unlocked) */
    private final Map<String, Integer> levels = new HashMap<>();

    public int getLevel(String skillId) { return levels.getOrDefault(skillId, 0); }

    public void setLevel(String skillId, int level) {
        if (level <= 0) levels.remove(skillId);
        else levels.put(skillId, level);
    }

    public boolean hasUnlocked(String skillId) { return levels.containsKey(skillId); }

    public Map<String, Integer> getAllLevels() { return Collections.unmodifiableMap(levels); }
}
