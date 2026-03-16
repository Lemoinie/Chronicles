package com.test.chronicles.core.storage;

import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.core.ModuleRegistry;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Flat-file YAML storage. Each player gets: players/{uuid}.yml
 * Each module writes to its own top-level section, ensuring isolated data.
 * Uses atomic save (write tmp → rename) to prevent corrupt files on crash.
 */
public class YamlStorageBackend implements StorageBackend {

    private final File playersDir;
    private final ModuleRegistry registry;
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public YamlStorageBackend(File dataFolder, ModuleRegistry registry) {
        this.playersDir = new File(dataFolder, "players");
        this.registry   = registry;
        if (!playersDir.exists()) playersDir.mkdirs();
    }

    @Override
    public PlayerProfile load(UUID uuid) {
        File file = new File(playersDir, uuid + ".yml");
        PlayerProfile profile = new PlayerProfile(uuid);
        if (!file.exists()) return profile;

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (ChroniclesModule m : registry.getModules()) {
            var section = cfg.getConfigurationSection(m.getId());
            if (section == null) continue;
            try { m.deserialize(profile, section); }
            catch (Exception ex) { LOG.warning("Deserialize failed [" + m.getId() + "] for " + uuid + ": " + ex.getMessage()); }
        }
        return profile;
    }

    @Override
    public void save(PlayerProfile profile) {
        YamlConfiguration cfg = new YamlConfiguration();
        for (ChroniclesModule m : registry.getModules()) {
            try { m.serialize(profile, cfg.createSection(m.getId())); }
            catch (Exception ex) { LOG.warning("Serialize failed [" + m.getId() + "] for " + profile.getUuid() + ": " + ex.getMessage()); }
        }
        // Atomic write: temp file → rename
        File tmp  = new File(playersDir, profile.getUuid() + ".tmp");
        File dest = new File(playersDir, profile.getUuid() + ".yml");
        try {
            cfg.save(tmp);
            if (dest.exists()) dest.delete();
            tmp.renameTo(dest);
        } catch (IOException ex) {
            LOG.severe("Failed to save profile " + profile.getUuid() + ": " + ex.getMessage());
        }
    }

    @Override
    public void shutdown() { /* no background threads */ }
}
