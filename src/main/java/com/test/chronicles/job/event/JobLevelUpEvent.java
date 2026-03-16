package com.test.chronicles.job.event;

import com.test.chronicles.api.event.ChroniclesEvent;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.job.Job;

public class JobLevelUpEvent extends ChroniclesEvent {
    private final PlayerProfile profile;
    private final Job job;
    private final int oldLevel, newLevel;
    public JobLevelUpEvent(PlayerProfile profile, Job job, int oldLevel, int newLevel) {
        this.profile = profile; this.job = job; this.oldLevel = oldLevel; this.newLevel = newLevel;
    }
    public PlayerProfile getProfile() { return profile; }
    public Job getJob() { return job; }
    public int getOldLevel() { return oldLevel; }
    public int getNewLevel() { return newLevel; }
}
