package com.test.chronicles.ability;

/**
 * An active ability a player can cast.
 *
 * Example YAML (data/abilities/meteor_strike.yml):
 * <pre>
 * id: meteor_strike
 * display-name: "Meteor Strike"
 * description: "Calls down a meteor."
 * cooldown-seconds: 30
 * mana-cost: 50
 * required-class: mage
 * required-skill: fireball
 * </pre>
 */
public interface Ability {
    String getId();
    String getDisplayName();
    String getDescription();
    int getCooldownSeconds();
    int getManaCost();
    /** Class required to use this ability. Null = no restriction. */
    String getRequiredClass();
    /** Skill prerequisite. Null = no restriction. */
    String getRequiredSkill();
}
