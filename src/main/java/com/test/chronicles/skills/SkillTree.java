package com.test.chronicles.skills;

import java.util.*;

/**
 * Directed prerequisite graph for skills.
 * Decides whether a player can unlock a skill based on what they've already learned.
 */
public class SkillTree {

    private final SkillRegistry registry;

    public SkillTree(SkillRegistry registry) { this.registry = registry; }

    /**
     * Returns true if all prerequisites for {@code skillId} are satisfied
     * and the player hasn't already reached max level.
     */
    public boolean canUnlock(PlayerSkillData data, String skillId) {
        Skill skill = registry.get(skillId);
        if (skill == null) return false;
        if (data.getLevel(skillId) >= skill.getMaxLevel()) return false;
        for (String prereq : skill.getPrerequisites()) {
            if (!data.hasUnlocked(prereq)) return false;
        }
        return true;
    }

    /** Returns all skills currently unlockable for this player. */
    public List<String> getUnlockable(PlayerSkillData data) {
        List<String> result = new ArrayList<>();
        for (Skill skill : registry.getAll()) {
            if (canUnlock(data, skill.getId())) result.add(skill.getId());
        }
        return result;
    }
}
