package com.voxencore.player;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/** Thread-safe repository useful for tests and local fallback storage. */
public final class InMemoryPlayerRepository implements PlayerRepository {
  private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();
  private final Map<String, UUID> names = new ConcurrentHashMap<>();

  @Override
  public CompletableFuture<Optional<PlayerProfile>> findById(UUID uniqueId) {
    return CompletableFuture.completedFuture(Optional.ofNullable(profiles.get(uniqueId)));
  }

  @Override
  public CompletableFuture<Optional<PlayerProfile>> findByLastKnownName(String username) {
    UUID uniqueId = names.get(normalize(username));
    return CompletableFuture.completedFuture(uniqueId == null ? Optional.empty() : Optional.ofNullable(profiles.get(uniqueId)));
  }

  @Override
  public CompletableFuture<Void> save(PlayerProfile profile) {
    profiles.put(profile.uniqueId(), profile);
    names.put(normalize(profile.identity().lastKnownName()), profile.uniqueId());
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Void> saveAll(Collection<PlayerProfile> profiles) {
    for (PlayerProfile profile : profiles) {
      this.profiles.put(profile.uniqueId(), profile);
      names.put(normalize(profile.identity().lastKnownName()), profile.uniqueId());
    }
    return CompletableFuture.completedFuture(null);
  }

  private static String normalize(String username) {
    return username.toLowerCase(Locale.ROOT);
  }
}
