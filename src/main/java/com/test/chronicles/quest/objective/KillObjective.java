package com.test.chronicles.quest.objective;

import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.quest.Objective;
import org.bukkit.entity.EntityType;

/** Objective: kill N mobs of a given EntityType. */
public class KillObjective implements Objective {

    private final String id, description;
    private final EntityType entityType;
    private final int required;

    public KillObjective(String id, EntityType entityType, int required) {
        this.id = id;
        this.entityType = entityType;
        this.required = required;
        this.description = "Kill " + required + " " + entityType.name().toLowerCase().replace("_", " ") + "(s)";
    }

    @Override public String getId() { return id; }
    @Override public String getDescription() { return description; }
    @Override public int getRequiredAmount() { return required; }

    public EntityType getEntityType() { return entityType; }

    @Override
    public boolean isComplete(PlayerProfile profile, String questId) {
        return profile.getQuestData().getProgress(questId, id) >= required;
    }
}
