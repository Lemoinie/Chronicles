package com.test.chronicles.attributes;

import java.util.*;

/**
 * Holds a player's base and computed final attribute values.
 * Supports batched updates — call beginBatch() before a set of changes,
 * then endBatch() to fire exactly one recalculation.
 */
public class AttributeMap {

    private final EnumMap<AttributeType, Double> base  = new EnumMap<>(AttributeType.class);
    private final EnumMap<AttributeType, Double> finals = new EnumMap<>(AttributeType.class);
    private final List<AttributeModifier> modifiers = new ArrayList<>();

    private boolean batching = false;
    private boolean dirty    = false;

    /** Called by AttributeModule after a full recalculation; fires AttributeRecalculateEvent. */
    private Runnable onRecalculate;

    public void setOnRecalculate(Runnable r) { this.onRecalculate = r; }

    // ─── Base values ─────────────────────────────────────────────────────────

    public void setBase(AttributeType type, double value) { base.put(type, value); markDirty(); }
    public double getBase(AttributeType type) { return base.getOrDefault(type, 0.0); }

    /** Final computed value = (base + flat) × (1 + percent). */
    public double getFinal(AttributeType type) { return finals.getOrDefault(type, getBase(type)); }

    // ─── Modifiers ───────────────────────────────────────────────────────────

    public void addModifier(AttributeModifier m) { modifiers.add(m); markDirty(); }

    public void removeModifiersFromSource(String source) {
        if (modifiers.removeIf(m -> m.source().equals(source))) markDirty();
    }

    public List<AttributeModifier> getModifiers() { return Collections.unmodifiableList(modifiers); }

    // ─── Batching ────────────────────────────────────────────────────────────

    public void beginBatch() { batching = true; }

    public void endBatch() { batching = false; if (dirty) recalculate(); }

    public boolean isBatching() { return batching; }

    // ─── Internals ───────────────────────────────────────────────────────────

    private void markDirty() { dirty = true; if (!batching) recalculate(); }

    private void recalculate() {
        dirty = false;
        for (AttributeType t : AttributeType.values()) {
            double b = base.getOrDefault(t, 0.0), flat = 0, pct = 0;
            for (AttributeModifier m : modifiers) {
                if (m.type() != t) continue;
                if (m.modifierType() == ModifierType.FLAT) flat += m.value();
                else pct += m.value();
            }
            finals.put(t, (b + flat) * (1.0 + pct));
        }
        if (onRecalculate != null) onRecalculate.run();
    }

    /** Snapshot for serialization. */
    public Map<AttributeType, Double> getBaseValues() { return Collections.unmodifiableMap(base); }
}
