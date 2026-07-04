package com.voxencore.config;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/** Coordinates asynchronous loading, validation, migration, and hot reload of configuration. */
public final class ConfigurationManager {
  private final ConfigurationSource source;
  private final ConfigurationMigrationEngine migrationEngine;
  private final List<ConfigurationValidator> validators;
  private final int targetVersion;
  private final AtomicReference<ConfigurationDocument> current = new AtomicReference<>();

  /** Creates a manager for a single configuration document. */
  public ConfigurationManager(
      ConfigurationSource source,
      ConfigurationMigrationEngine migrationEngine,
      List<ConfigurationValidator> validators,
      int targetVersion) {
    this.source = Objects.requireNonNull(source, "source");
    this.migrationEngine = Objects.requireNonNull(migrationEngine, "migrationEngine");
    this.validators = List.copyOf(validators);
    this.targetVersion = targetVersion;
  }

  /** Loads, migrates, validates, stores, and exposes the current document. */
  public CompletableFuture<ConfigurationDocument> load() {
    return source.load().thenCompose(this::prepare);
  }

  /** Performs a hot reload and atomically swaps the active document after validation. */
  public CompletableFuture<ConfigurationDocument> reload() {
    return load();
  }

  /** Returns the active document. */
  public ConfigurationDocument current() {
    ConfigurationDocument document = current.get();
    if (document == null) {
      throw new IllegalStateException("Configuration has not been loaded");
    }
    return document;
  }

  private CompletableFuture<ConfigurationDocument> prepare(ConfigurationDocument loaded) {
    ConfigurationDocument migrated = migrationEngine.migrate(loaded, targetVersion);
    for (ConfigurationValidator validator : validators) {
      validator.validate(migrated);
    }
    current.set(migrated);
    if (migrated.version() != loaded.version()) {
      return source.save(migrated).thenApply(ignored -> migrated);
    }
    return CompletableFuture.completedFuture(migrated);
  }
}
