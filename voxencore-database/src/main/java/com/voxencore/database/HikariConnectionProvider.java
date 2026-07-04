package com.voxencore.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

/** HikariCP-backed provider that integrates when HikariCP is present on the runtime classpath. */
public final class HikariConnectionProvider implements ConnectionProvider {
  private final Object dataSource;

  /** Creates a HikariCP data source using reflection to keep this module buildable without vendoring. */
  public HikariConnectionProvider(DatabaseSettings settings) {
    try {
      Class<?> configType = Class.forName("com.zaxxer.hikari.HikariConfig");
      Object config = configType.getConstructor().newInstance();
      configType.getMethod("setJdbcUrl", String.class).invoke(config, settings.jdbcUrl());
      configType.getMethod("setUsername", String.class).invoke(config, settings.username());
      configType.getMethod("setPassword", String.class).invoke(config, settings.password());
      configType.getMethod("setMaximumPoolSize", int.class).invoke(config, settings.maximumPoolSize());
      configType.getMethod("setDriverClassName", String.class).invoke(config, settings.type().driverClassName());
      Class<?> dataSourceType = Class.forName("com.zaxxer.hikari.HikariDataSource");
      dataSource = dataSourceType.getConstructor(configType).newInstance(config);
    } catch (ClassNotFoundException exception) {
      throw new IllegalStateException("HikariCP is required on the runtime classpath", exception);
    } catch (IllegalAccessException | InstantiationException | NoSuchMethodException
        | InvocationTargetException exception) {
      throw new IllegalStateException("Unable to initialize HikariCP", exception);
    }
  }

  @Override
  public Connection connection() throws SQLException {
    try {
      return (Connection) dataSource.getClass().getMethod("getConnection").invoke(dataSource);
    } catch (IllegalAccessException | NoSuchMethodException exception) {
      throw new SQLException("Unable to borrow HikariCP connection", exception);
    } catch (InvocationTargetException exception) {
      Throwable cause = exception.getCause();
      if (cause instanceof SQLException sqlException) {
        throw sqlException;
      }
      throw new SQLException("Unable to borrow HikariCP connection", cause);
    }
  }

  @Override
  public void close() {
    try {
      dataSource.getClass().getMethod("close").invoke(dataSource);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
      throw new IllegalStateException("Unable to close HikariCP data source", exception);
    }
  }
}
