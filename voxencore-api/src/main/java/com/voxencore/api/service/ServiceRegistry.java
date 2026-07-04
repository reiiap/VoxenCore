package com.voxencore.api.service;

import java.util.Optional;

/** Registry and service locator for VoxenCore service contracts. */
public interface ServiceRegistry {
  /** Registers a service instance for a key. */
  <T> void register(ServiceKey<T> key, T service);

  /** Resolves a required service by key. */
  <T> T require(ServiceKey<T> key);

  /** Resolves an optional service by key. */
  <T> Optional<T> find(ServiceKey<T> key);
}
