package com.test.chronicles.quest;

import java.util.*;

/** Per-player quest progress and completion data. Owned by QuestModule. */
public class PlayerQuestData {

    private final Set<String> completed = new HashSet<>();
    /** questId → (objectiveId → amount progress) */
    private final Map<String, Map<String, Integer>> active = new HashMap<>();

    public boolean hasCompleted(String questId) { return completed.contains(questId); }
    public void markCompleted(String questId) { completed.add(questId); active.remove(questId); }

    public boolean isActive(String questId) { return active.containsKey(questId); }

    public void startQuest(String questId) {
        if (!active.containsKey(questId)) active.put(questId, new HashMap<>());
    }

    public int getProgress(String questId, String objectiveId) {
        return active.getOrDefault(questId, Map.of()).getOrDefault(objectiveId, 0);
    }

    public void addProgress(String questId, String objectiveId, int amount) {
        active.computeIfAbsent(questId, k -> new HashMap<>())
              .merge(objectiveId, amount, Integer::sum);
    }

    public Set<String> getActiveQuestIds() { return Collections.unmodifiableSet(active.keySet()); }
    public Set<String> getCompleted() { return Collections.unmodifiableSet(completed); }
}
