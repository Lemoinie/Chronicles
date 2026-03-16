package com.test.chronicles.profile;

import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.core.storage.StorageBackend;
import com.test.chronicles.profile.event.ProfileLoadedEvent;
import com.test.chronicles.profile.event.ProfileSavedEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.*;

/**
 * In-memory cache of online player profiles.
 * All I/O runs on a dedicated single-thread executor; Bukkit callbacks hop back to main thread.
 */
public class ProfileManager {

    private final Map<UUID, PlayerProfile> cache = new ConcurrentHashMap<>();
    private final StorageBackend storage;
    private final ModuleEventBus bus;
    private final Plugin plugin;
    private final ExecutorService io = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Chronicles-IO"); t.setDaemon(true); return t;
    });

    public ProfileManager(StorageBackend storage, ModuleEventBus bus, Plugin plugin) {
        this.storage = storage; this.bus = bus; this.plugin = plugin;
    }

    /** Load async → cache → fire ProfileLoadedEvent on main thread. */
    public CompletableFuture<PlayerProfile> loadAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> storage.load(uuid), io)
            .thenApply(p -> {
                cache.put(uuid, p);
                Bukkit.getScheduler().runTask(plugin, () -> bus.publish(new ProfileLoadedEvent(p)));
                return p;
            });
    }

    /** Save async → fire ProfileSavedEvent on main thread. */
    public CompletableFuture<Void> saveAsync(PlayerProfile profile) {
        return CompletableFuture.runAsync(() -> storage.save(profile), io)
            .thenRun(() -> Bukkit.getScheduler().runTask(plugin, () -> bus.publish(new ProfileSavedEvent(profile))));
    }

    public PlayerProfile get(UUID uuid) { return cache.get(uuid); }

    public void unload(UUID uuid) { cache.remove(uuid); }

    public Collection<PlayerProfile> getAll() { return Collections.unmodifiableCollection(cache.values()); }

    /** Block until all cached profiles are written. Called on plugin shutdown. */
    public void shutdown() {
        cache.values().forEach(storage::save);
        storage.shutdown();
        io.shutdown();
        try { io.awaitTermination(10, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
    }
}
