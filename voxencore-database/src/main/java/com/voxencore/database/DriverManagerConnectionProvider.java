package com.voxencore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Direct DriverManager provider for lightweight tests and embedded databases. */
public final class DriverManagerConnectionProvider implements ConnectionProvider {
  private final DatabaseSettings settings;

  /** Creates a provider from settings. */
  public DriverManagerConnectionProvider(DatabaseSettings settings) {
    this.settings = settings;
  }

  @Override
  public Connection connection() throws SQLException {
    return DriverManager.getConnection(settings.jdbcUrl(), settings.username(), settings.password());
  }

  @Override
  public void close() {}
}
