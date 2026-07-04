package com.voxencore.config;

/** Migrates a configuration document from one schema version to the next. */
public interface ConfigurationMigration {
  /** Returns the source version handled by this migration. */
  int fromVersion();

  /** Returns the target version produced by this migration. */
  int toVersion();

  /** Applies the migration. */
  ConfigurationDocument migrate(ConfigurationDocument document);
}
