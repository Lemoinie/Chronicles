package com.test.chronicles.quest;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.quest.event.QuestCompleteEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;
import java.util.Set;

public class QuestModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private final QuestRegistry registry = new QuestRegistry();

    public QuestModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "quests"; }
    @Override public List<String> getDependencies() { return List.of("profile"); }

    @Override
    public void onEnable(Chronicles plugin) {
        registry.loadFromFolder(new File(plugin.getDataFolder(), "data/quests"));
    }

    @Override public void onDisable() {}

    public QuestRegistry getRegistry() { return registry; }

    public boolean startQuest(PlayerProfile profile, String questId) {
        Quest quest = registry.get(questId);
        if (quest == null || profile.getQuestData().hasCompleted(questId) || profile.getQuestData().isActive(questId)) return false;
        profile.getQuestData().startQuest(questId);
        return true;
    }

    /** Advance objective progress and check for quest completion. */
    public void addProgress(PlayerProfile profile, String questId, String objectiveId, int amount) {
        if (!profile.getQuestData().isActive(questId)) return;
        Quest quest = registry.get(questId);
        if (quest == null) return;

        profile.getQuestData().addProgress(questId, objectiveId, amount);

        // Check if all objectives are now complete
        boolean allDone = quest.getObjectives().stream()
                .allMatch(o -> o.isComplete(profile, questId));
        if (allDone) {
            profile.getQuestData().markCompleted(questId);
            bus.publish(new QuestCompleteEvent(profile, quest));
        }
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        section.set("completed", List.copyOf(profile.getQuestData().getCompleted()));
        Set<String> active = profile.getQuestData().getActiveQuestIds();
        for (String questId : active) {
            Quest quest = registry.get(questId);
            if (quest == null) continue;
            for (Objective obj : quest.getObjectives()) {
                section.set("active." + questId + "." + obj.getId(),
                        profile.getQuestData().getProgress(questId, obj.getId()));
            }
        }
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        for (String id : section.getStringList("completed")) profile.getQuestData().markCompleted(id);
        var activeSection = section.getConfigurationSection("active");
        if (activeSection == null) return;
        for (String questId : activeSection.getKeys(false)) {
            profile.getQuestData().startQuest(questId);
            var progSection = activeSection.getConfigurationSection(questId);
            if (progSection == null) continue;
            for (String objId : progSection.getKeys(false))
                profile.getQuestData().addProgress(questId, objId, progSection.getInt(objId));
        }
    }
}
