package com.test.chronicles;

import com.test.chronicles.ability.AbilityModule;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.core.ModuleRegistry;
import com.test.chronicles.job.JobModule;
import com.test.chronicles.origin.OriginModule;
import com.test.chronicles.playerclass.ClassModule;
import com.test.chronicles.profile.ProfileModule;
import com.test.chronicles.quest.QuestModule;
import com.test.chronicles.skills.SkillModule;
import com.test.chronicles.stats.StatModule;
import org.bukkit.plugin.java.JavaPlugin;

public class Chronicles extends JavaPlugin {

    private ModuleEventBus eventBus;
    private ModuleRegistry moduleRegistry;

    @Override
    public void onEnable() {
        eventBus      = new ModuleEventBus();
        moduleRegistry = new ModuleRegistry(this);

        // Registration order matters for getDependencies(), but the registry
        // will topologically sort regardless — this is just for clarity.
        moduleRegistry.register(new ProfileModule(eventBus));
        moduleRegistry.register(new StatModule(eventBus));
        moduleRegistry.register(new OriginModule(eventBus));
        moduleRegistry.register(new ClassModule(eventBus));
        moduleRegistry.register(new SkillModule(eventBus));
        moduleRegistry.register(new JobModule(eventBus));
        moduleRegistry.register(new AbilityModule(eventBus));
        moduleRegistry.register(new QuestModule(eventBus));

        moduleRegistry.enableAll();

        getLogger().info("Chronicles enabled — " + moduleRegistry.count() + " modules loaded.");
    }

    @Override
    public void onDisable() {
        if (moduleRegistry != null) moduleRegistry.disableAll();
        getLogger().info("Chronicles disabled.");
    }

    public ModuleEventBus getEventBus()        { return eventBus; }
    public ModuleRegistry getModuleRegistry()  { return moduleRegistry; }
}
