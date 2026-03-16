package com.test.chronicles.stats;

/**
 * An immutable stat modifier. Identified by source so it can be removed cleanly
 * (e.g. when a class is changed or an item is unequipped).
 */
public record StatModifier(String source, StatType type, double value, ModifierType modifierType) {}
