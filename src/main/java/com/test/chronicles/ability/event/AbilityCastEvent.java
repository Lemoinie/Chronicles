package com.test.chronicles.ability.event;

import com.test.chronicles.ability.Ability;
import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;

public class AbilityCastEvent implements ChroniclesEvent {
    private final PlayerProfile profile;
    private final Ability ability;
    public AbilityCastEvent(PlayerProfile profile, Ability ability) {
        this.profile = profile; this.ability = ability;
    }
    public PlayerProfile getProfile() { return profile; }
    public Ability getAbility() { return ability; }
}
