package com.test.chronicles.origin.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.origin.Origin;

public class OriginChangeEvent implements ChroniclesEvent {
    private final PlayerProfile profile;
    private final Origin oldOrigin, newOrigin;
    public OriginChangeEvent(PlayerProfile profile, Origin oldOrigin, Origin newOrigin) {
        this.profile = profile; this.oldOrigin = oldOrigin; this.newOrigin = newOrigin;
    }
    public PlayerProfile getProfile() { return profile; }
    public Origin getOldOrigin() { return oldOrigin; }
    public Origin getNewOrigin() { return newOrigin; }
}
