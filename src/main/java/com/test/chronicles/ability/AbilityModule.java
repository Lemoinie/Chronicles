package com.test.chronicles.ability;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.event.ModuleEventBus;
import com.test.chronicles.api.module.ChroniclesModule;
import com.test.chronicles.api.profile.PlayerProfile;
import com.test.chronicles.ability.event.AbilityCastEvent;
import com.test.chronicles.stats.StatType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class AbilityModule implements ChroniclesModule {

    private final ModuleEventBus bus;
    private final AbilityRegistry registry = new AbilityRegistry();
    private final CooldownManager cooldowns = new CooldownManager();

    public AbilityModule(ModuleEventBus bus) { this.bus = bus; }

    @Override public String getId() { return "abilities"; }
    @Override public List<String> getDependencies() { return List.of("stats", "skills", "class"); }

    @Override
    public void onEnable(Chronicles plugin) {
        registry.loadFromFolder(new File(plugin.getDataFolder(), "data/abilities"));
    }

    @Override public void onDisable() {}

    public AbilityRegistry getRegistry() { return registry; }
    public CooldownManager getCooldowns() { return cooldowns; }

    /**
     * Attempt to cast an ability. Validates cooldown and mana, then fires AbilityCastEvent.
     * @return true if cast succeeded.
     */
    public boolean cast(Player player, PlayerProfile profile, String abilityId) {
        Ability ability = registry.get(abilityId);
        if (ability == null) return false;
        if (!profile.getAbilityData().hasUnlocked(abilityId)) return false;
        if (cooldowns.isOnCooldown(player.getUniqueId(), abilityId)) return false;

        double currentMana = profile.getStats().getFinal(StatType.MANA);
        if (currentMana < ability.getManaCost()) return false;

        // Deduct mana base value and apply cooldown
        profile.getStats().setBase(StatType.MANA, profile.getStats().getBase(StatType.MANA) - ability.getManaCost());
        cooldowns.setCooldown(player.getUniqueId(), abilityId, ability.getCooldownSeconds());
        bus.publish(new AbilityCastEvent(profile, ability));
        return true;
    }

    @Override
    public void serialize(PlayerProfile profile, ConfigurationSection section) {
        section.set("unlocked", List.copyOf(profile.getAbilityData().getUnlocked()));
        section.set("equipped", profile.getAbilityData().getEquipped());
    }

    @Override
    public void deserialize(PlayerProfile profile, ConfigurationSection section) {
        for (String id : section.getStringList("unlocked")) profile.getAbilityData().unlock(id);
        List<String> equipped = section.getStringList("equipped");
        for (int i = 0; i < equipped.size(); i++) {
            if (equipped.get(i) != null && !equipped.get(i).isEmpty())
                profile.getAbilityData().equip(equipped.get(i), i);
        }
    }
}
