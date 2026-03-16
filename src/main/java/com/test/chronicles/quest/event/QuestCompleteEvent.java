package com.test.chronicles.quest.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.quest.Quest;

public class QuestCompleteEvent implements ChroniclesEvent {
    private final PlayerProfile profile;
    private final Quest quest;
    public QuestCompleteEvent(PlayerProfile profile, Quest quest) {
        this.profile = profile; this.quest = quest;
    }
    public PlayerProfile getProfile() { return profile; }
    public Quest getQuest() { return quest; }
}
