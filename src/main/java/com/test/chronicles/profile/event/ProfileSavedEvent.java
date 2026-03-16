package com.test.chronicles.profile.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;

public class ProfileSavedEvent extends ChroniclesEvent {
    private final PlayerProfile profile;
    public ProfileSavedEvent(PlayerProfile profile) { this.profile = profile; }
    public PlayerProfile getProfile() { return profile; }
}
