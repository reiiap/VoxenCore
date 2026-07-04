package com.voxencore.config;

import java.util.Comparator;
import java.util.List;

/** Applies ordered configuration migrations until a target version is reached. */
public final class ConfigurationMigrationEngine {
  private final List<ConfigurationMigration> migrations;

  /** Creates a migration engine from available migrations. */
  public ConfigurationMigrationEngine(List<ConfigurationMigration> migrations) {
    this.migrations = migrations.stream().sorted(Comparator.comparingInt(ConfigurationMigration::fromVersion)).toList();
  }

  /** Migrates the document to the requested version. */
  public ConfigurationDocument migrate(ConfigurationDocument document, int targetVersion) {
    ConfigurationDocument current = document;
    while (current.version() < targetVersion) {
      int version = current.version();
      ConfigurationMigration migration = migrations.stream()
          .filter(candidate -> candidate.fromVersion() == version)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("No configuration migration from version " + version));
      current = migration.migrate(current);
      if (current.version() != migration.toVersion()) {
        throw new IllegalStateException("Migration produced unexpected version " + current.version());
      }
    }
    return current;
  }
}
