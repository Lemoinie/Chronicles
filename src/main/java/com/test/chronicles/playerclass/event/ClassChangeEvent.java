package com.test.chronicles.playerclass.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.playerclass.PlayerClass;

public class ClassChangeEvent implements ChroniclesEvent {
    private final PlayerProfile profile;
    private final PlayerClass oldClass, newClass;
    public ClassChangeEvent(PlayerProfile p, PlayerClass o, PlayerClass n) {
        this.profile = p; this.oldClass = o; this.newClass = n;
    }
    public PlayerProfile getProfile() { return profile; }
    public PlayerClass getOldClass() { return oldClass; }
    public PlayerClass getNewClass() { return newClass; }
}
