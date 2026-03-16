package com.test.chronicles.profile;

import com.test.chronicles.api.profile.PlayerProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    private final ProfileManager manager;

    public ProfileListener(ProfileManager manager) { this.manager = manager; }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        manager.loadAsync(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        PlayerProfile profile = manager.get(e.getPlayer().getUniqueId());
        if (profile == null) return;
        manager.saveAsync(profile)
               .thenRun(() -> manager.unload(e.getPlayer().getUniqueId()));
    }
}
