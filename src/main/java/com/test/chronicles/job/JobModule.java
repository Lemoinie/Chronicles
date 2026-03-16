package com.test.chronicles.job;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.job.event.JobLevelUpEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public class JobModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private final JobRegistry registry = new JobRegistry();

    public JobModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "jobs"; }
    @Override public List<String> getDependencies() { return List.of("profile"); }

    @Override
    public void onEnable(Chronicles plugin) {
        registry.loadFromFolder(new File(plugin.getDataFolder(), "data/jobs"));
    }

    @Override public void onDisable() {}

    public JobRegistry getRegistry() { return registry; }

    /** Add XP to a job, leveling up automatically and firing JobLevelUpEvent. */
    public void addXp(PlayerProfile profile, String jobId, long amount) {
        Job job = registry.get(jobId);
        if (job == null) return;
        PlayerJobData data = profile.getJobData();
        int currentLevel = data.getLevel(jobId);
        if (currentLevel >= job.getMaxLevel()) return;

        long newXp = data.getXp(jobId) + amount;
        data.setXp(jobId, newXp);

        // Check for level-ups (handles multiple levels at once)
        while (currentLevel < job.getMaxLevel()) {
            long needed = job.getXpForLevel(currentLevel + 1);
            if (newXp < needed) break;
            newXp -= needed;
            data.setXp(jobId, newXp);
            int newLevel = currentLevel + 1;
            data.setLevel(jobId, newLevel);
            bus.publish(new JobLevelUpEvent(profile, job, currentLevel, newLevel));
            currentLevel = newLevel;
        }
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        profile.getJobData().getAllXp().forEach((id, xp) -> {
            section.set(id + ".xp", xp);
            section.set(id + ".level", profile.getJobData().getLevel(id));
        });
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        for (String jobId : section.getKeys(false)) {
            profile.getJobData().setXp(jobId, section.getLong(jobId + ".xp", 0));
            profile.getJobData().setLevel(jobId, section.getInt(jobId + ".level", 0));
        }
    }
}
