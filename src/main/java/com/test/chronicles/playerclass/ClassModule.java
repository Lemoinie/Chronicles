package com.test.chronicles.playerclass;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.playerclass.event.ClassChangeEvent;
import com.test.chronicles.profile.event.ProfileLoadedEvent;
import com.test.chronicles.stats.StatModifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public class ClassModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private final ClassRegistry registry = new ClassRegistry();

    public ClassModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "class"; }
    @Override public List<String> getDependencies() { return List.of("stats", "origin"); }

    @Override
    public void onEnable(Chronicles plugin) {
        registry.loadFromFolder(new File(plugin.getDataFolder(), "data/classes"));
        bus.subscribe(ProfileLoadedEvent.class, e -> applyClass(e.getProfile()));
    }

    @Override public void onDisable() {}

    public ClassRegistry getRegistry() { return registry; }

    public void setClass(PlayerProfile profile, PlayerClass newClass) {
        PlayerClass old = registry.get(profile.getClassId());
        profile.getStats().beginBatch();
        if (old != null) profile.getStats().removeModifiersFromSource("class:" + old.getId());
        for (StatModifier m : newClass.getStatModifiers()) profile.getStats().addModifier(m);
        profile.getStats().endBatch();
        profile.setClassId(newClass.getId());
        bus.publish(new ClassChangeEvent(profile, old, newClass));
    }

    private void applyClass(PlayerProfile profile) {
        if (profile.getClassId() == null) return;
        PlayerClass cls = registry.get(profile.getClassId());
        if (cls == null) return;
        profile.getStats().beginBatch();
        for (StatModifier m : cls.getStatModifiers()) profile.getStats().addModifier(m);
        profile.getStats().endBatch();
    }

    @Override
    public void serialize(PlayerProfile p, ConfigurationSection s) {
        if (p.getClassId() != null) s.set("id", p.getClassId());
    }

    @Override
    public void deserialize(PlayerProfile p, ConfigurationSection s) {
        p.setClassId(s.getString("id"));
    }
}
