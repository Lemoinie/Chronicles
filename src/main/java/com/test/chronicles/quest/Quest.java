package com.test.chronicles.quest;

import java.util.List;

/**
 * A quest with one or more objectives and rewards.
 *
 * Example YAML (data/quests/first_steps.yml):
 * <pre>
 * id: first_steps
 * display-name: "First Steps"
 * description: "Get started on your adventure."
 * objectives:
 *   - id: kill_zombies
 *     type: KILL
 *     target: ZOMBIE
 *     amount: 5
 *   - id: collect_wood
 *     type: COLLECT
 *     target: OAK_LOG
 *     amount: 10
 * rewards:
 *   xp: 100
 * </pre>
 */
public interface Quest {
    String getId();
    String getDisplayName();
    String getDescription();
    List<Objective> getObjectives();
}
