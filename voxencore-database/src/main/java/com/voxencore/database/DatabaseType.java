package com.voxencore.database;

/** Supported relational database engines. */
public enum DatabaseType {
  SQLITE("org.sqlite.JDBC"),
  MYSQL("com.mysql.cj.jdbc.Driver"),
  MARIADB("org.mariadb.jdbc.Driver"),
  POSTGRESQL("org.postgresql.Driver");

  private final String driverClassName;

  DatabaseType(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  /** Returns the JDBC driver class name normally used by this engine. */
  public String driverClassName() {
    return driverClassName;
  }
}
