# Chronicles Project Wiki

Welcome to the Chronicles Wiki. This document provides technical details on the project's architecture, API, and how to contribute.

## 🏗️ Modular Architecture

Chronicles is built on a strictly modular architecture. Each feature (Stats, Skills, etc.) is a standalone `ChroniclesModule`.

### Key Design Principles
- **Isolation**: Modules never hold direct references to each other.
- **Inter-Module Communication**: All communication happens through the `ModuleEventBus`.
- **Topological Loading**: Modules specify dependencies and are enabled in the correct order.
- **Independent Serialization**: Each module handles its own slice of the `PlayerProfile` data in the YAML configuration.

### Module Lifecycle
1. **Registration**: Done in `Chronicles#onEnable` via `ModuleRegistry#register`.
2. **Topological Sort**: `ModuleRegistry` calculates the enable order based on `getDependencies()`.
3. **Enable**: `onEnable(Chronicles plugin)` is called for each module.
4. **Disable**: `onDisable()` is called in reverse order when the plugin shuts down.

---

## 🧩 Core Modules

| Module | Description |
| :--- | :--- |
| **Profile** | Manages core player data and persistent state. |
| **Stats** | Handles primary attributes (Strength, Vitality, etc.) and calculations. |
| **Origin** | Handles player races/backgrounds and their permanent modifiers. |
| **Class** | Manages RPG classes and level progression. |
| **Skill** | Tracks skill points and unlocked active/passive skills. |
| **Job** | Handles professions and gathering/crafting progression. |
| **Ability** | Logic for casting and managing cooldowns of active abilities. |
| **Quest** | System for tracking progress and rewards for in-game tasks. |

---

## 🔌 API Guide

### Accessing Chronicles
To interact with Chronicles, get the main plugin instance:
```java
Chronicles chronicles = (Chronicles) Bukkit.getPluginManager().getPlugin("Chronicles");
```

### Using the Event Bus
The `ModuleEventBus` is the primary way to listen for or trigger actions.
```java
// Subscribe to an event
chronicles.getEventBus().subscribe(StatRecalculateEvent.class, event -> {
    // Handle event
});

// Publish an event
chronicles.getEventBus().publish(new MyCustomEvent(player));
```

### Retrieving a Module
If you need specific module logic, you can retrieve it from the registry:
```java
StatModule stats = (StatModule) chronicles.getModuleRegistry().getModule("stats");
```

---

## 🤝 How to Contribute

We welcome contributions! Please follow these guidelines to maintain the project's integrity.

### Adding a New Module
1. Create a new package in `com.test.chronicles.yourmodule`.
2. Implement the `ChroniclesModule` interface.
3. Register your module in `Chronicles#onEnable`.
4. Use the `ModuleEventBus` for any interaction with other modules. **Do not add direct dependencies between modules.**

### Build Process
We use Gradle for dependency management and building.
- **Install Wrapper**: `./gradlew wrapper --gradle-version 8.8` (already installed).
- **Build**: `./gradlew build`
- **Output**: The jar will be in `build/libs/`.

### Coding Standards
- Use the provided `ModuleEventBus` for all internal events.
- Keep the `api` package clean and well-documented.
- Ensure all new features are data-driven (using YAML configurations where applicable).
