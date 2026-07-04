package com.voxencore.database;

import java.sql.SQLException;

/** Function variant that may throw SQLException. */
@FunctionalInterface
public interface SqlFunction<T, R> {
  /** Applies the function. */
  R apply(T value) throws SQLException;
}
