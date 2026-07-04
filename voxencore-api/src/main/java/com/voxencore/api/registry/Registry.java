package com.voxencore.api.registry;

import java.util.Optional;
import java.util.Set;

/** Named registry for immutable runtime definitions such as menus, items, and packets. */
public interface Registry<T> {
  /** Registers a value under a stable namespaced key. */
  void register(String key, T value);

  /** Finds a registered value by key. */
  Optional<T> find(String key);

  /** Returns an immutable view of registered keys. */
  Set<String> keys();
}
