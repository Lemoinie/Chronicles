package com.test.chronicles.stats.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;

/** Fired after a player's stats have been fully recomputed. Subscribe to apply derived effects. */
public class StatRecalculateEvent extends ChroniclesEvent {
    private final PlayerProfile profile;
    public StatRecalculateEvent(PlayerProfile profile) { this.profile = profile; }
    public PlayerProfile getProfile() { return profile; }
}
