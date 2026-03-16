package com.test.chronicles.quest;

import com.test.chronicles.api.profile.PlayerProfile;

/**
 * A single trackable objective within a quest.
 * Implementations check progress against their specific trigger type.
 */
public interface Objective {
    String getId();
    String getDescription();
    /** Amount of progress needed to complete this objective. */
    int getRequiredAmount();
    /** Check if this objective is complete for the given profile. */
    boolean isComplete(PlayerProfile profile, String questId);
}
