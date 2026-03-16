package com.test.chronicles.skills;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.skills.event.SkillUnlockEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public class SkillModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private final SkillRegistry registry = new SkillRegistry();
    private SkillTree tree;

    public SkillModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "skills"; }
    @Override public List<String> getDependencies() { return List.of("stats", "class"); }

    @Override
    public void onEnable(Chronicles plugin) {
        registry.loadFromFolder(new File(plugin.getDataFolder(), "data/skills"));
        tree = new SkillTree(registry);
    }

    @Override public void onDisable() {}

    public SkillRegistry getRegistry() { return registry; }
    public SkillTree getTree() { return tree; }

    /**
     * Attempt to unlock or level up a skill.
     * @return true if successful, false if prerequisites not met or already max level.
     */
    public boolean unlockSkill(PlayerProfile profile, String skillId) {
        if (!tree.canUnlock(profile.getSkillData(), skillId)) return false;
        int newLevel = profile.getSkillData().getLevel(skillId) + 1;
        profile.getSkillData().setLevel(skillId, newLevel);
        bus.publish(new SkillUnlockEvent(profile, registry.get(skillId), newLevel));
        return true;
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        profile.getSkillData().getAllLevels().forEach((id, level) -> section.set(id, level));
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        for (String key : section.getKeys(false))
            profile.getSkillData().setLevel(key, section.getInt(key));
    }
}
