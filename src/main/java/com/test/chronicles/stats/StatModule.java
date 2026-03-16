package com.test.chronicles.stats;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.stats.event.StatRecalculateEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class StatModule implements ChroniclesModule {

    private final ModuleEventBus bus;

    public StatModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "stats"; }
    @Override public List<String> getDependencies() { return List.of("profile"); }

    @Override public void onEnable(Chronicles plugin) {}
    @Override public void onDisable() {}

    /**
     * Wire a freshly-loaded profile's StatMap to publish StatRecalculateEvent.
     * Called by OriginModule / ClassModule after applying initial modifiers.
     */
    public void initProfile(PlayerProfile profile) {
        profile.getStats().setOnRecalculate(() -> bus.publish(new StatRecalculateEvent(profile)));
    }

    /** Batch-safe modifier addition. Always go through this, not StatMap directly. */
    public void addModifier(PlayerProfile profile, StatModifier modifier) {
        profile.getStats().addModifier(modifier);
    }

    public void removeModifiersFromSource(PlayerProfile profile, String source) {
        profile.getStats().removeModifiersFromSource(source);
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        profile.getStats().getBaseValues().forEach((t, v) -> section.set(t.name(), v));
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        for (StatType type : StatType.values()) {
            if (section.contains(type.name()))
                profile.getStats().setBase(type, section.getDouble(type.name()));
        }
    }
}
