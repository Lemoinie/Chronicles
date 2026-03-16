package com.test.chronicles.attributes.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;

/**
 * Fired whenever a player's attribute modifiers change or a recalculation occurs.
 */
public record AttributeRecalculateEvent(PlayerProfile profile) implements ChroniclesEvent {}
