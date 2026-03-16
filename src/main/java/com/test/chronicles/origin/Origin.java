package com.test.chronicles.origin;

import com.test.chronicles.attributes.AttributeModifier;

import java.util.List;

/**
 * Defines a player's race/origin.
 * All stat bonuses are expressed as StatModifiers so they integrate cleanly with StatMap.
 */
public interface Origin {
    String getId();
    String getDisplayName();
    String getDescription();
    List<AttributeModifier> getAttributeModifiers();
}
