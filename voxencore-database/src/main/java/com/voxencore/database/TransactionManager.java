package com.voxencore.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Executes JDBC units of work inside asynchronous transactions. */
public final class TransactionManager {
  private final ConnectionProvider provider;
  private final Executor executor;

  /** Creates a transaction manager. */
  public TransactionManager(ConnectionProvider provider, Executor executor) {
    this.provider = provider;
    this.executor = executor;
  }

  /** Runs the supplied operation inside a transaction. */
  public <T> CompletableFuture<T> transaction(SqlFunction<Connection, T> operation) {
    return CompletableFuture.supplyAsync(() -> runTransaction(operation), executor);
  }

  private <T> T runTransaction(SqlFunction<Connection, T> operation) {
    try (Connection connection = provider.connection()) {
      boolean previousAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
      try {
        T result = operation.apply(connection);
        connection.commit();
        return result;
      } catch (SQLException | RuntimeException exception) {
        connection.rollback();
        throw exception;
      } finally {
        connection.setAutoCommit(previousAutoCommit);
      }
    } catch (SQLException exception) {
      throw new IllegalStateException("Database transaction failed", exception);
    }
  }
}
