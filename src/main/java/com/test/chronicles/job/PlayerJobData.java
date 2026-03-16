package com.test.chronicles.job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Per-player job XP and level data. Owned by JobModule. */
public class PlayerJobData {

    private final Map<String, Long> xp = new HashMap<>();
    private final Map<String, Integer> levels = new HashMap<>();

    public long getXp(String jobId) { return xp.getOrDefault(jobId, 0L); }
    public int getLevel(String jobId) { return levels.getOrDefault(jobId, 0); }

    public void setXp(String jobId, long value) { xp.put(jobId, Math.max(0, value)); }
    public void setLevel(String jobId, int level) { levels.put(jobId, Math.max(0, level)); }

    public Map<String, Long> getAllXp() { return Collections.unmodifiableMap(xp); }
    public Map<String, Integer> getAllLevels() { return Collections.unmodifiableMap(levels); }
}
