package com.test.chronicles.playerclass;

import com.test.chronicles.stats.StatModifier;

import java.util.List;

/**
 * A combat class (Warrior, Mage, etc.).
 * Provides base stat modifiers and restricts which skills the player may unlock.
 */
public interface PlayerClass {
    String getId();
    String getDisplayName();
    String getDescription();
    List<StatModifier> getStatModifiers();
    /** Skill IDs this class is allowed to use. Empty = no restriction. */
    List<String> getAllowedSkills();
}
