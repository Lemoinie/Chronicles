package com.test.chronicles.stats;

public enum ModifierType {
    /** Added directly to base. Applied before percent modifiers. */
    FLAT,
    /** Applied after flat additions. A value of 0.2 means +20%. */
    PERCENT
}
