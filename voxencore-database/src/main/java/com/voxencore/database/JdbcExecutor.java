package com.voxencore.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Runs blocking JDBC operations on a dedicated executor and exposes CompletableFuture results. */
public final class JdbcExecutor {
  private final ConnectionProvider provider;
  private final Executor executor;

  /** Creates an executor from a connection provider and async executor. */
  public JdbcExecutor(ConnectionProvider provider, Executor executor) {
    this.provider = provider;
    this.executor = executor;
  }

  /** Executes a read or write operation with a managed connection. */
  public <T> CompletableFuture<T> execute(SqlFunction<Connection, T> operation) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = provider.connection()) {
        return operation.apply(connection);
      } catch (SQLException exception) {
        throw new IllegalStateException("Database operation failed", exception);
      }
    }, executor);
  }
}
