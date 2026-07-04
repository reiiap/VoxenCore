package com.voxencore.api.service;

import java.util.Objects;

/** Identifies a service by its public contract type and a stable name. */
public final class ServiceKey<T> {
  private final Class<T> type;
  private final String name;

  private ServiceKey(Class<T> type, String name) {
    this.type = Objects.requireNonNull(type, "type");
    this.name = Objects.requireNonNull(name, "name");
  }

  /** Creates a key for the default implementation of the supplied type. */
  public static <T> ServiceKey<T> of(Class<T> type) {
    return new ServiceKey<>(type, type.getName());
  }

  /** Creates a named service key for the supplied type. */
  public static <T> ServiceKey<T> named(Class<T> type, String name) {
    return new ServiceKey<>(type, name);
  }

  /** Returns the service contract type. */
  public Class<T> type() {
    return type;
  }

  /** Returns the stable service name. */
  public String name() {
    return name;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof ServiceKey<?> serviceKey)) {
      return false;
    }
    return type.equals(serviceKey.type) && name.equals(serviceKey.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name);
  }
}
