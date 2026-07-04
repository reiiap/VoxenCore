package com.voxencore.database;

import java.util.Objects;

/** Immutable database connection settings. */
public final class DatabaseSettings {
  private final DatabaseType type;
  private final String jdbcUrl;
  private final String username;
  private final String password;
  private final int maximumPoolSize;

  private DatabaseSettings(Builder builder) {
    type = Objects.requireNonNull(builder.type, "type");
    jdbcUrl = Objects.requireNonNull(builder.jdbcUrl, "jdbcUrl");
    username = builder.username;
    password = builder.password;
    maximumPoolSize = builder.maximumPoolSize;
  }

  /** Returns the database type. */
  public DatabaseType type() { return type; }

  /** Returns the JDBC URL. */
  public String jdbcUrl() { return jdbcUrl; }

  /** Returns the database username. */
  public String username() { return username; }

  /** Returns the database password. */
  public String password() { return password; }

  /** Returns the maximum pool size. */
  public int maximumPoolSize() { return maximumPoolSize; }

  /** Creates a settings builder. */
  public static Builder builder(DatabaseType type, String jdbcUrl) {
    return new Builder(type, jdbcUrl);
  }

  /** Builder for database settings. */
  public static final class Builder {
    private final DatabaseType type;
    private final String jdbcUrl;
    private String username = "";
    private String password = "";
    private int maximumPoolSize = 10;

    private Builder(DatabaseType type, String jdbcUrl) {
      this.type = type;
      this.jdbcUrl = jdbcUrl;
    }

    /** Sets credentials. */
    public Builder credentials(String username, String password) {
      this.username = Objects.requireNonNull(username, "username");
      this.password = Objects.requireNonNull(password, "password");
      return this;
    }

    /** Sets the maximum pool size. */
    public Builder maximumPoolSize(int maximumPoolSize) {
      if (maximumPoolSize < 1) {
        throw new IllegalArgumentException("maximumPoolSize must be positive");
      }
      this.maximumPoolSize = maximumPoolSize;
      return this;
    }

    /** Builds immutable settings. */
    public DatabaseSettings build() {
      return new DatabaseSettings(this);
    }
  }
}
