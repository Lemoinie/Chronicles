package com.test.chronicles.quest;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/** Registry for Quest objects. Quests can be registered in code or loaded from YAML. */
public class QuestRegistry {

    private final Map<String, Quest> quests = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    /** Folder-based YAML loading is delegated to a YamlQuest parser (implement as needed). */
    public void loadFromFolder(File folder) {
        if (!folder.exists()) folder.mkdirs();
        // TODO: implement YamlQuest.fromFile() similar to YamlJob / YamlSkill
        LOG.info("[Quests] Folder scanning not yet implemented; register quests in code.");
    }

    public void register(Quest quest) { quests.put(quest.getId(), quest); LOG.info("[Quests] Registered: " + quest.getId()); }
    public Quest get(String id) { return quests.get(id); }
    public Collection<Quest> getAll() { return Collections.unmodifiableCollection(quests.values()); }
}
