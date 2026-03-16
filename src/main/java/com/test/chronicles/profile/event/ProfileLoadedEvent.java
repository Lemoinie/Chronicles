package com.test.chronicles.profile.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;

public class ProfileLoadedEvent implements ChroniclesEvent {
    private final PlayerProfile profile;
    public ProfileLoadedEvent(PlayerProfile profile) { this.profile = profile; }
    public PlayerProfile getProfile() { return profile; }
}
