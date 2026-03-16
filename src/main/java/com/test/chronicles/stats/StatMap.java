package com.test.chronicles.stats;

import java.util.*;

/**
 * Holds a player's base and computed final stat values.
 * Supports batched updates — call beginBatch() before a set of changes,
 * then endBatch() to fire exactly one recalculation.
 */
public class StatMap {

    private final EnumMap<StatType, Double> base  = new EnumMap<>(StatType.class);
    private final EnumMap<StatType, Double> finals = new EnumMap<>(StatType.class);
    private final List<StatModifier> modifiers = new ArrayList<>();

    private boolean batching = false;
    private boolean dirty    = false;

    /** Called by StatModule after a full recalculation; fires StatRecalculateEvent. */
    private Runnable onRecalculate;

    public void setOnRecalculate(Runnable r) { this.onRecalculate = r; }

    // ─── Base values ─────────────────────────────────────────────────────────

    public void setBase(StatType type, double value) { base.put(type, value); markDirty(); }
    public double getBase(StatType type) { return base.getOrDefault(type, 0.0); }

    /** Final computed value = (base + flat) × (1 + percent). */
    public double getFinal(StatType type) { return finals.getOrDefault(type, getBase(type)); }

    // ─── Modifiers ───────────────────────────────────────────────────────────

    public void addModifier(StatModifier m) { modifiers.add(m); markDirty(); }

    public void removeModifiersFromSource(String source) {
        if (modifiers.removeIf(m -> m.source().equals(source))) markDirty();
    }

    public List<StatModifier> getModifiers() { return Collections.unmodifiableList(modifiers); }

    // ─── Batching ────────────────────────────────────────────────────────────

    public void beginBatch() { batching = true; }

    public void endBatch() { batching = false; if (dirty) recalculate(); }

    public boolean isBatching() { return batching; }

    // ─── Internals ───────────────────────────────────────────────────────────

    private void markDirty() { dirty = true; if (!batching) recalculate(); }

    private void recalculate() {
        dirty = false;
        for (StatType t : StatType.values()) {
            double b = base.getOrDefault(t, 0.0), flat = 0, pct = 0;
            for (StatModifier m : modifiers) {
                if (m.type() != t) continue;
                if (m.modifierType() == ModifierType.FLAT) flat += m.value();
                else pct += m.value();
            }
            finals.put(t, (b + flat) * (1.0 + pct));
        }
        if (onRecalculate != null) onRecalculate.run();
    }

    /** Snapshot for serialization. */
    public Map<StatType, Double> getBaseValues() { return Collections.unmodifiableMap(base); }
}
