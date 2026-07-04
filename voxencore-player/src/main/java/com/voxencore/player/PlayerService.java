package com.voxencore.player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Public asynchronous player platform service contract. */
public interface PlayerService {
  /** Loads or returns an online player profile. */
  CompletableFuture<PlayerProfile> load(PlayerIdentity identity);

  /** Returns an online profile when currently loaded. */
  Optional<PlayerProfile> online(UUID uniqueId);

  /** Looks up a profile from online cache, offline cache, or repository. */
  CompletableFuture<Optional<PlayerProfile>> lookup(UUID uniqueId);

  /** Finds a profile by last known name. */
  CompletableFuture<Optional<PlayerProfile>> lookup(String username);

  /** Returns a player snapshot from the active session when present. */
  Optional<PlayerSnapshot> snapshot(UUID uniqueId);

  /** Saves a player asynchronously when loaded. */
  CompletableFuture<Void> save(UUID uniqueId);

  /** Unloads and saves a player asynchronously. */
  CompletableFuture<Void> unload(UUID uniqueId);
}
