package com.voxencore.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Maps entities to and from JDBC primitives. */
public interface EntityMapper<T> {
  /** Reads an entity from the current result-set row. */
  T read(ResultSet resultSet) throws SQLException;

  /** Writes entity fields into a prepared statement. */
  void write(PreparedStatement statement, T entity) throws SQLException;
}
