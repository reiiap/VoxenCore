# VoxenCore

VoxenCore is an enterprise-grade Paper framework plugin for high-population Minecraft survival networks. It is designed as a modular Java 21 platform that centralizes shared services such as caching, scheduling, threading, player data, packet abstractions, menus, placeholders, economy, holograms, NPCs, scoreboards, tab lists, configuration, and integrations.

## Requirements

- Java 21
- Paper 1.21.x
- Gradle 8.x

## Architecture

The repository uses a Gradle Kotlin DSL multi-module layout. Each module owns one bounded context and exposes contracts through `voxencore-api` where cross-module access is required.

Current foundation modules include:

- `voxencore-api` for service, module, event bus, and registry contracts.
- `voxencore-core` for the Paper plugin entry point and framework bootstrap.
- `voxencore-cache` for cache abstractions and memory cache implementation.
- `voxencore-scheduler` for centralized scheduling contracts and baseline executor implementation.
- `voxencore-thread` for dedicated subsystem executor pools.

Additional bounded-context modules are present for database, packet, player, GUI, item, hologram, NPC, menu, placeholder, economy, resource pack, contract, dungeon, fishing, announcement, scoreboard, tab, configuration, and utility systems.

## Development

Build all modules:

```bash
gradle build
```

The codebase targets Java 21, uses Java only for production code, and keeps public APIs documented with Javadocs.

## Paper API Dependency Strategy

`voxencore-core` is compiled against a local compile-only Paper bootstrap stub in `voxencore-paper-stub` so the project remains buildable in restricted CI environments where external Maven repositories are blocked. The stub is not packaged into the core plugin artifact and exists only to model the small bootstrap surface needed by the current foundation phase. Production Paper API usage should be added through official Paper API dependencies as subsystem adapters are implemented.

## Phase 2 Core Infrastructure

The second implementation phase adds foundational infrastructure services:

- `voxencore-config` provides asynchronous configuration loading, hot reload, validation, and ordered schema migration.
- `voxencore-database` provides JDBC abstractions, supported provider factories for SQLite, MySQL, MariaDB, and PostgreSQL, HikariCP runtime integration, repository contracts, entity mapping, migrations, and transaction execution.
- `voxencore-core` provides logging, metrics, module loading, hook management, and lifecycle management services registered during plugin bootstrap.

## Phase 3 Player Platform

The third implementation phase adds the `voxencore-player` platform with immutable profile DTOs, session management, expiring player cache namespaces, cooldowns, asynchronous repository/service contracts, dirty-session batch saving, disconnect save support, crash-recovery checkpoints, and framework player events.
