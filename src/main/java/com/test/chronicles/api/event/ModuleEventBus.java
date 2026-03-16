package com.test.chronicles.api.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * In-process pub/sub bus for inter-module communication.
 * Modules never hold references to each other — they only hold a bus reference.
 */
public class ModuleEventBus {

    private final Map<Class<? extends ChroniclesEvent>, List<Consumer<ChroniclesEvent>>> listeners = new HashMap<>();
    private static final Logger LOG = Logger.getLogger("Chronicles/EventBus");

    @SuppressWarnings("unchecked")
    public <T extends ChroniclesEvent> void subscribe(Class<T> type, Consumer<T> handler) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>())
                 .add((Consumer<ChroniclesEvent>) (Consumer<?>) handler);
    }

    public <T extends ChroniclesEvent> void publish(T event) {
        List<Consumer<ChroniclesEvent>> handlers = listeners.get(event.getClass());
        if (handlers == null) return;
        for (Consumer<ChroniclesEvent> h : handlers) {
            try { h.accept(event); }
            catch (Exception ex) {
                LOG.severe("[EventBus] Exception handling " + event.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        }
    }

    public void clear() { listeners.clear(); }
}
