package com.test.chronicles.skills;

/**
 * A learnable skill — either passive (always active) or active (triggered by ability).
 *
 * Example YAML (data/skills/fireball.yml):
 * <pre>
 * id: fireball
 * display-name: "Fireball"
 * description: "Hurls a ball of fire."
 * max-level: 5
 * prerequisites: []
 * allowed-classes:
 *   - mage
 *   - wizard
 * </pre>
 */
public interface Skill {
    String getId();
    String getDisplayName();
    String getDescription();
    int getMaxLevel();
    /** IDs of skills that must be unlocked first. */
    java.util.List<String> getPrerequisites();
    /** Classes that may learn this skill. Empty = no restriction. */
    java.util.List<String> getAllowedClasses();
}
