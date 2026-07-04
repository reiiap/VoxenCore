package com.voxencore.database;

/** Factory methods for supported database connection providers. */
public final class DatabaseProviders {
  private DatabaseProviders() {}

  /** Creates a SQLite provider. */
  public static ConnectionProvider sqlite(String jdbcUrl) {
    return pooled(DatabaseSettings.builder(DatabaseType.SQLITE, jdbcUrl).maximumPoolSize(1).build());
  }

  /** Creates a MySQL provider. */
  public static ConnectionProvider mysql(String jdbcUrl, String username, String password) {
    return pooled(DatabaseSettings.builder(DatabaseType.MYSQL, jdbcUrl).credentials(username, password).build());
  }

  /** Creates a MariaDB provider. */
  public static ConnectionProvider mariaDb(String jdbcUrl, String username, String password) {
    return pooled(DatabaseSettings.builder(DatabaseType.MARIADB, jdbcUrl).credentials(username, password).build());
  }

  /** Creates a PostgreSQL provider. */
  public static ConnectionProvider postgresql(String jdbcUrl, String username, String password) {
    return pooled(DatabaseSettings.builder(DatabaseType.POSTGRESQL, jdbcUrl).credentials(username, password).build());
  }

  /** Creates a HikariCP-backed provider. */
  public static ConnectionProvider pooled(DatabaseSettings settings) {
    return new HikariConnectionProvider(settings);
  }
}
