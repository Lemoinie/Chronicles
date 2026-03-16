package com.test.chronicles.profile;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.core.storage.YamlStorageBackend;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ProfileModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private ProfileManager manager;

    public ProfileModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "profile"; }
    @Override public List<String> getDependencies() { return List.of(); }

    @Override
    public void onEnable(Chronicles plugin) {
        var storage = new YamlStorageBackend(plugin.getDataFolder(), plugin.getModuleRegistry());
        manager = new ProfileManager(storage, bus, plugin);
        plugin.getServer().getPluginManager().registerEvents(new ProfileListener(manager), plugin);
    }

    @Override
    public void onDisable() { if (manager != null) manager.shutdown(); }

    public ProfileManager getManager() { return manager; }

    // Profile module stores no data of its own; sub-modules handle their sections.
    @Override public void serialize(PlayerProfile p, ConfigurationSection s) {}
    @Override public void deserialize(PlayerProfile p, ConfigurationSection s) {}
}
