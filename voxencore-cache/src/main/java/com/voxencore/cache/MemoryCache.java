package com.voxencore.cache;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/** Thread-safe in-memory cache with time-based expiration. */
public final class MemoryCache<K, V> implements Cache<K, V> {
  private final Map<K, Entry<V>> entries = new ConcurrentHashMap<>();
  private final Duration timeToLive;
  private final Clock clock;

  /** Creates a cache using the system UTC clock. */
  public MemoryCache(Duration timeToLive) {
    this(timeToLive, Clock.systemUTC());
  }

  /** Creates a cache using an explicit clock for deterministic operation. */
  public MemoryCache(Duration timeToLive, Clock clock) {
    this.timeToLive = Objects.requireNonNull(timeToLive, "timeToLive");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public Optional<V> getIfPresent(K key) {
    Entry<V> entry = entries.get(key);
    if (entry == null) {
      return Optional.empty();
    }
    if (entry.expiresAtMillis() <= clock.millis()) {
      entries.remove(key, entry);
      return Optional.empty();
    }
    return Optional.of(entry.value());
  }

  @Override
  public CompletableFuture<V> get(K key, Function<K, CompletableFuture<V>> loader) {
    return getIfPresent(key)
        .map(CompletableFuture::completedFuture)
        .orElseGet(() -> loader.apply(key).thenApply(value -> {
          put(key, value);
          return value;
        }));
  }

  @Override
  public void put(K key, V value) {
    entries.put(key, new Entry<>(value, clock.millis() + timeToLive.toMillis()));
  }

  @Override
  public void invalidate(K key) {
    entries.remove(key);
  }

  @Override
  public void invalidateAll() {
    entries.clear();
  }

  private record Entry<V>(V value, long expiresAtMillis) {}
}
