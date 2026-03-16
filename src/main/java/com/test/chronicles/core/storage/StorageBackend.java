package com.test.chronicles.core.storage;

import com.test.chronicles.api.profile.PlayerProfile;

import java.util.UUID;

/**
 * Persistence contract for player profiles.
 * All implementations must be thread-safe — these methods are called off the main thread.
 */
public interface StorageBackend {
    /** Load a profile. Returns a fresh profile if none exists. */
    PlayerProfile load(UUID uuid);

    /** Save a profile atomically. */
    void save(PlayerProfile profile);

    /** Flush any pending writes and release resources. */
    void shutdown();
}
