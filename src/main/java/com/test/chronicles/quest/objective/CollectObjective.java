package com.test.chronicles.quest.objective;

import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.quest.Objective;
import org.bukkit.Material;

/** Objective: collect N items of a given Material. */
public class CollectObjective implements Objective {

    private final String id, description;
    private final Material material;
    private final int required;

    public CollectObjective(String id, Material material, int required) {
        this.id = id;
        this.material = material;
        this.required = required;
        this.description = "Collect " + required + " " + material.name().toLowerCase().replace("_", " ");
    }

    @Override public String getId() { return id; }
    @Override public String getDescription() { return description; }
    @Override public int getRequiredAmount() { return required; }

    public Material getMaterial() { return material; }

    @Override
    public boolean isComplete(PlayerProfile profile, String questId) {
        return profile.getQuestData().getProgress(questId, id) >= required;
    }
}
