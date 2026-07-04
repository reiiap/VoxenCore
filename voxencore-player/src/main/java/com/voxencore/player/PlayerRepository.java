package com.voxencore.player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Asynchronous repository for player profiles. */
public interface PlayerRepository {
  /** Finds a profile by unique id. */
  CompletableFuture<Optional<PlayerProfile>> findById(UUID uniqueId);

  /** Finds the most recent profile using a cached last known name. */
  CompletableFuture<Optional<PlayerProfile>> findByLastKnownName(String username);

  /** Saves one profile. */
  CompletableFuture<Void> save(PlayerProfile profile);

  /** Saves many profiles in one repository operation. */
  CompletableFuture<Void> saveAll(Collection<PlayerProfile> profiles);
}
