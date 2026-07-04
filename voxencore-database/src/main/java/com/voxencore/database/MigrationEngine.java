package com.voxencore.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Applies database migrations once and records schema state transactionally. */
public final class MigrationEngine {
  private final TransactionManager transactions;
  private final List<DatabaseMigration> migrations;

  /** Creates a migration engine. */
  public MigrationEngine(TransactionManager transactions, List<DatabaseMigration> migrations) {
    this.transactions = transactions;
    this.migrations = migrations.stream().sorted(Comparator.comparingInt(DatabaseMigration::version)).toList();
  }

  /** Applies all pending migrations asynchronously. */
  public CompletableFuture<Void> migrate() {
    return transactions.transaction(connection -> {
      ensureSchemaTable(connection);
      int currentVersion = currentVersion(connection);
      for (DatabaseMigration migration : migrations) {
        if (migration.version() > currentVersion) {
          migration.apply(connection);
          record(connection, migration);
        }
      }
      return null;
    });
  }

  private static void ensureSchemaTable(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate("create table if not exists voxencore_schema_migrations ("
          + "version integer primary key, name varchar(190) not null, applied_at timestamp not null)");
    }
  }

  private static int currentVersion(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select max(version) from voxencore_schema_migrations")) {
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
      return 0;
    }
  }

  private static void record(Connection connection, DatabaseMigration migration) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "insert into voxencore_schema_migrations(version, name, applied_at) values (?, ?, current_timestamp)")) {
      statement.setInt(1, migration.version());
      statement.setString(2, migration.name());
      statement.executeUpdate();
    }
  }
}
