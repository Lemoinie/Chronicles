package com.test.chronicles.api.module;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.profile.PlayerProfile;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.List;

/**
 * Contract every Chronicles module must implement.
 * Each module owns exactly one slice of PlayerProfile data and serializes it independently.
 */
public interface ChroniclesModule {

    /** Unique key used as the YAML save-file section for this module's data. */
    String getId();

    void onEnable(Chronicles plugin);
    void onDisable();

    /** Module IDs that must be enabled before this one. Default: none. */
    default List<String> getDependencies() { return Collections.emptyList(); }

    /** Write this module's player data into {@code section}. */
    void serialize(PlayerProfile profile, ConfigurationSection section);

    /** Read this module's player data from {@code section}. */
    void deserialize(PlayerProfile profile, ConfigurationSection section);
}
