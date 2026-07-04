package com.voxencore.database;

import java.sql.Connection;
import java.sql.SQLException;

/** Provides JDBC connections and owns any required backing resources. */
public interface ConnectionProvider extends AutoCloseable {
  /** Borrows a JDBC connection. */
  Connection connection() throws SQLException;

  /** Closes provider resources. */
  @Override
  void close();
}
