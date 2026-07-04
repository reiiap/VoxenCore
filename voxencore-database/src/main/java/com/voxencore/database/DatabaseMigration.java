package com.voxencore.database;

import java.sql.Connection;
import java.sql.SQLException;

/** Ordered database schema migration. */
public interface DatabaseMigration {
  /** Returns the unique migration version. */
  int version();

  /** Returns a descriptive migration name. */
  String name();

  /** Applies the migration using the supplied connection. */
  void apply(Connection connection) throws SQLException;
}
