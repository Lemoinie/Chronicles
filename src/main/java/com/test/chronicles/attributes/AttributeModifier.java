package com.test.chronicles.attributes;

/**
 * An immutable attribute modifier. Identified by source so it can be removed cleanly.
 */
public record AttributeModifier(String source, AttributeType type, double value, ModifierType modifierType) {}
