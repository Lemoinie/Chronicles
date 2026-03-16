package com.test.chronicles.job;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class JobRegistry {
    private final Map<String, Job> jobs = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public void loadFromFolder(File folder) {
        if (!folder.exists()) { folder.mkdirs(); return; }
        File[] files = folder.listFiles((d, n) -> n.endsWith(".yml"));
        if (files == null) return;
        for (File f : files) {
            try { Job j = YamlJob.fromFile(f); jobs.put(j.getId(), j); LOG.info("[Jobs] Loaded: " + j.getId()); }
            catch (Exception ex) { LOG.warning("[Jobs] Failed: " + f.getName() + ": " + ex.getMessage()); }
        }
    }

    public void register(Job job) { jobs.put(job.getId(), job); }
    public Job get(String id) { return jobs.get(id); }
    public Collection<Job> getAll() { return Collections.unmodifiableCollection(jobs.values()); }
}
