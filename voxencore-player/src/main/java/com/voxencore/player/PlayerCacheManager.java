package com.voxencore.player;

import com.voxencore.cache.MemoryCache;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** Manages dedicated expiring player caches and asynchronous warming. */
public final class PlayerCacheManager {
  private final Map<PlayerCacheType, MemoryCache<UUID, Object>> caches = new EnumMap<>(PlayerCacheType.class);

  /** Creates cache namespaces with a shared expiration duration. */
  public PlayerCacheManager(Duration expiration) {
    for (PlayerCacheType type : PlayerCacheType.values()) {
      caches.put(type, new MemoryCache<>(expiration));
    }
  }

  /** Stores a value in a named player cache. */
  public void put(PlayerCacheType type, UUID uniqueId, Object value) {
    caches.get(type).put(uniqueId, value);
  }

  /** Resolves a value from cache or warms it asynchronously. */
  public <T> CompletableFuture<T> get(
      PlayerCacheType type, UUID uniqueId, Class<T> valueType, Function<UUID, CompletableFuture<T>> loader) {
    return caches.get(type).get(uniqueId, key -> loader.apply(key).thenApply(valueType::cast)).thenApply(valueType::cast);
  }

  /** Preloads all derived profile cache namespaces for the supplied profile. */
  public CompletableFuture<Void> preload(PlayerProfile profile) {
    UUID uniqueId = profile.uniqueId();
    put(PlayerCacheType.PROFILE, uniqueId, profile);
    put(PlayerCacheType.STATISTICS, uniqueId, profile.statistics());
    put(PlayerCacheType.METADATA, uniqueId, profile.metadata());
    put(PlayerCacheType.SETTINGS, uniqueId, profile.settings());
    return CompletableFuture.completedFuture(null);
  }

  /** Invalidates every cache namespace for a player. */
  public void invalidate(UUID uniqueId) {
    caches.values().forEach(cache -> cache.invalidate(uniqueId));
  }
}
