package com.test.chronicles.skills.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.skills.Skill;

public class SkillUnlockEvent extends ChroniclesEvent {
    private final PlayerProfile profile;
    private final Skill skill;
    private final int newLevel;
    public SkillUnlockEvent(PlayerProfile profile, Skill skill, int newLevel) {
        this.profile = profile; this.skill = skill; this.newLevel = newLevel;
    }
    public PlayerProfile getProfile() { return profile; }
    public Skill getSkill() { return skill; }
    public int getNewLevel() { return newLevel; }
}
