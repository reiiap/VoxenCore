package com.voxencore.cache;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** Asynchronous cache abstraction with expiration-friendly read-through loading. */
public interface Cache<K, V> {
  /** Returns the cached value when it is present and valid. */
  Optional<V> getIfPresent(K key);

  /** Returns a value from cache or loads and stores it asynchronously. */
  CompletableFuture<V> get(K key, Function<K, CompletableFuture<V>> loader);

  /** Stores a value in the cache. */
  void put(K key, V value);

  /** Invalidates a single key. */
  void invalidate(K key);

  /** Invalidates all cached values. */
  void invalidateAll();
}
