package com.test.chronicles.core;

import com.test.chronicles.Chronicles;
import com.test.chronicles.api.module.ChroniclesModule;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages registration, topological sort, enable, and disable of all modules.
 * Modules are enabled in dependency order and disabled in reverse order.
 */
public class ModuleRegistry {

    private final Chronicles plugin;
    private final Map<String, ChroniclesModule> modules = new LinkedHashMap<>();
    private final List<ChroniclesModule> enableOrder = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger("Chronicles");

    public ModuleRegistry(Chronicles plugin) { this.plugin = plugin; }

    public void register(ChroniclesModule module) { modules.put(module.getId(), module); }

    public void enableAll() {
        for (ChroniclesModule m : topologicalSort()) {
            try {
                m.onEnable(plugin);
                enableOrder.add(m);
                LOG.info("[Chronicles] " + capitalize(m.getId()) + " enabled successfully.");
            } catch (Exception ex) {
                LOG.severe("[Chronicles] Failed to enable: " + m.getId() + " — " + ex.getMessage());
            }
        }
    }

    public void disableAll() {
        ListIterator<ChroniclesModule> it = enableOrder.listIterator(enableOrder.size());
        while (it.hasPrevious()) {
            ChroniclesModule m = it.previous();
            try { 
                m.onDisable(); 
                LOG.info("[Chronicles] " + capitalize(m.getId()) + " disabled successfully."); 
            }
            catch (Exception ex) { LOG.severe("[Chronicles] Failed to disable: " + m.getId()); }
        }
        enableOrder.clear();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public ChroniclesModule getModule(String id) { return modules.get(id); }
    public Collection<ChroniclesModule> getModules() { return Collections.unmodifiableCollection(modules.values()); }
    public int count() { return modules.size(); }

    private List<ChroniclesModule> topologicalSort() {
        Map<String, Integer> inDeg = new HashMap<>();
        Map<String, List<String>> adj  = new HashMap<>();
        for (String id : modules.keySet()) { inDeg.put(id, 0); adj.put(id, new ArrayList<>()); }
        for (ChroniclesModule m : modules.values()) {
            for (String dep : m.getDependencies()) {
                if (!modules.containsKey(dep)) { LOG.warning("Unknown dependency '" + dep + "' for " + m.getId()); continue; }
                adj.get(dep).add(m.getId());
                inDeg.merge(m.getId(), 1, Integer::sum);
            }
        }
        Queue<String> q = new ArrayDeque<>();
        inDeg.forEach((id, deg) -> { if (deg == 0) q.add(id); });
        List<ChroniclesModule> result = new ArrayList<>();
        while (!q.isEmpty()) {
            String id = q.poll(); result.add(modules.get(id));
            for (String next : adj.get(id)) { if (inDeg.merge(next, -1, Integer::sum) == 0) q.add(next); }
        }
        if (result.size() != modules.size()) {
            LOG.severe("[Chronicles] Circular dependency! Falling back to registration order.");
            return new ArrayList<>(modules.values());
        }
        return result;
    }
}
