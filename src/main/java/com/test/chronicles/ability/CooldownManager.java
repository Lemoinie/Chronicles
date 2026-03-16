package com.test.chronicles.ability;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks per-player, per-ability cooldown expiry timestamps.
 * Thread-safe; safe to query from any thread.
 */
public class CooldownManager {

    /** uuid → (abilityId → expiry millis) */
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public boolean isOnCooldown(UUID uuid, String abilityId) {
        Map<String, Long> map = cooldowns.get(uuid);
        if (map == null) return false;
        Long expiry = map.get(abilityId);
        return expiry != null && System.currentTimeMillis() < expiry;
    }

    public long getRemainingMs(UUID uuid, String abilityId) {
        Map<String, Long> map = cooldowns.get(uuid);
        if (map == null) return 0;
        Long expiry = map.get(abilityId);
        if (expiry == null) return 0;
        return Math.max(0, expiry - System.currentTimeMillis());
    }

    public void setCooldown(UUID uuid, String abilityId, int seconds) {
        cooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                 .put(abilityId, System.currentTimeMillis() + (seconds * 1000L));
    }

    public void clearCooldown(UUID uuid, String abilityId) {
        Map<String, Long> map = cooldowns.get(uuid);
        if (map != null) map.remove(abilityId);
    }

    public void clearAll(UUID uuid) { cooldowns.remove(uuid); }
}
