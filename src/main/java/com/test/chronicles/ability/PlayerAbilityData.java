package com.test.chronicles.ability;

import java.util.*;

/** Per-player ability unlock and equipped slot data. Owned by AbilityModule. */
public class PlayerAbilityData {

    private final Set<String> unlocked = new HashSet<>();
    /** Ordered ability slots (null = empty slot). */
    private final List<String> equipped = new ArrayList<>(Collections.nCopies(6, null));

    public void unlock(String abilityId) { unlocked.add(abilityId); }
    public boolean hasUnlocked(String abilityId) { return unlocked.contains(abilityId); }
    public Set<String> getUnlocked() { return Collections.unmodifiableSet(unlocked); }

    public void equip(String abilityId, int slot) {
        if (slot < 0 || slot >= equipped.size()) return;
        equipped.set(slot, abilityId);
    }

    public void unequip(int slot) {
        if (slot >= 0 && slot < equipped.size()) equipped.set(slot, null);
    }

    public List<String> getEquipped() { return Collections.unmodifiableList(equipped); }
}
