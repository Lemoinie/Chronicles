package com.test.chronicles.origin;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.origin.event.OriginChangeEvent;
import com.test.chronicles.profile.event.ProfileLoadedEvent;
import com.test.chronicles.stats.StatModifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public class OriginModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private final OriginRegistry registry = new OriginRegistry();

    public OriginModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "origin"; }
    @Override public List<String> getDependencies() { return List.of("stats"); }

    @Override
    public void onEnable(Chronicles plugin) {
        File folder = new File(plugin.getDataFolder(), "data/origins");
        registry.loadFromFolder(folder);

        // On profile load, re-apply origin modifiers via batched update
        bus.subscribe(ProfileLoadedEvent.class, e -> applyOrigin(e.getProfile()));
    }

    @Override public void onDisable() {}

    public OriginRegistry getRegistry() { return registry; }

    public void setOrigin(PlayerProfile profile, Origin newOrigin) {
        Origin old = registry.get(profile.getOriginId());
        profile.getStats().beginBatch();
        if (old != null) profile.getStats().removeModifiersFromSource("origin:" + old.getId());
        for (StatModifier m : newOrigin.getStatModifiers()) profile.getStats().addModifier(m);
        profile.getStats().endBatch();
        profile.setOriginId(newOrigin.getId());
        bus.publish(new OriginChangeEvent(profile, old, newOrigin));
    }

    private void applyOrigin(PlayerProfile profile) {
        if (profile.getOriginId() == null) return;
        Origin origin = registry.get(profile.getOriginId());
        if (origin == null) return;
        profile.getStats().beginBatch();
        for (StatModifier m : origin.getStatModifiers()) profile.getStats().addModifier(m);
        profile.getStats().endBatch();
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        if (profile.getOriginId() != null) section.set("id", profile.getOriginId());
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        profile.setOriginId(section.getString("id"));
    }
}
