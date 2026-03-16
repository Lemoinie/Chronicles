package com.test.chronicles.job;

/**
 * A leveled gathering/crafting job.
 *
 * Example YAML (data/jobs/woodcutting.yml):
 * <pre>
 * id: woodcutting
 * display-name: "Woodcutting"
 * description: "Gather wood from trees."
 * max-level: 50
 * # XP required to reach each level (index 0 = level 1 threshold)
 * level-xp:
 *   - 100
 *   - 250
 *   - 500
 *   - 900
 * </pre>
 */
public interface Job {
    String getId();
    String getDisplayName();
    String getDescription();
    int getMaxLevel();
    /** XP required to reach {@code level} from level {@code level - 1}. */
    long getXpForLevel(int level);
}
