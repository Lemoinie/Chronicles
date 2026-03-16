package com.test.chronicles.api.profile;

import com.test.chronicles.ability.PlayerAbilityData;
import com.test.chronicles.job.PlayerJobData;
import com.test.chronicles.quest.PlayerQuestData;
import com.test.chronicles.skills.PlayerSkillData;
import com.test.chronicles.attributes.AttributeMap;

import java.util.UUID;

/**
 * Central data object for an online player.
 * Each module reads/writes only the sub-data block it owns.
 * Passed between systems; never held long-term by anything other than ProfileManager.
 */
public class PlayerProfile {

    private final UUID uuid;

    // --- Sub-data blocks: each owned by exactly one module ---
    private final AttributeMap      attributes = new AttributeMap();      // AttributeModule
    private final PlayerSkillData   skillData  = new PlayerSkillData();   // SkillModule
    private final PlayerJobData     jobData    = new PlayerJobData();     // JobModule
    private final PlayerAbilityData abilityData= new PlayerAbilityData(); // AbilityModule
    private final PlayerQuestData   questData  = new PlayerQuestData();   // QuestModule

    private String originId; // OriginModule
    private String classId;  // ClassModule

    public PlayerProfile(UUID uuid) { this.uuid = uuid; }

    public UUID   getUuid()      { return uuid; }

    public String getOriginId()              { return originId; }
    public void   setOriginId(String id)     { this.originId = id; }

    public String getClassId()               { return classId; }
    public void   setClassId(String id)      { this.classId = id; }

    public AttributeMap      getAttributes()  { return attributes; }
    public PlayerSkillData   getSkillData()   { return skillData; }
    public PlayerJobData     getJobData()     { return jobData; }
    public PlayerAbilityData getAbilityData() { return abilityData; }
    public PlayerQuestData   getQuestData()   { return questData; }
}
