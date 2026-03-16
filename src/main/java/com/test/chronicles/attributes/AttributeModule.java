package com.test.chronicles.attributes;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.attributes.event.AttributeRecalculateEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class AttributeModule implements ChroniclesModule {

    private final ModuleEventBus bus;

    public AttributeModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "attributes"; }
    @Override public List<String> getDependencies() { return List.of("profile"); }

    @Override public void onEnable(Chronicles plugin) {}
    @Override public void onDisable() {}

    /**
     * Wire a freshly-loaded profile's AttributeMap to publish AttributeRecalculateEvent.
     */
    public void initProfile(PlayerProfile profile) {
        profile.getAttributes().setOnRecalculate(() -> bus.publish(new AttributeRecalculateEvent(profile)));
    }

    /** Batch-safe modifier addition. Always go through this, not AttributeMap directly. */
    public void addModifier(PlayerProfile profile, AttributeModifier modifier) {
        profile.getAttributes().addModifier(modifier);
    }

    public void removeModifiersFromSource(PlayerProfile profile, String source) {
        profile.getAttributes().removeModifiersFromSource(source);
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        profile.getAttributes().getBaseValues().forEach((t, v) -> section.set(t.name(), v));
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        for (AttributeType type : AttributeType.values()) {
            if (section.contains(type.name()))
                profile.getAttributes().setBase(type, section.getDouble(type.name()));
        }
    }
}
