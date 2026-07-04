package com.voxencore.player;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Immutable identity data for a player, excluding plaintext network addresses. */
public record PlayerIdentity(
    UUID uniqueId,
    String username,
    String lastKnownName,
    Instant firstJoin,
    Instant lastJoin,
    String ipHash) {
  /** Creates validated identity data. */
  public PlayerIdentity {
    Objects.requireNonNull(uniqueId, "uniqueId");
    username = requireText(username, "username");
    lastKnownName = requireText(lastKnownName, "lastKnownName");
    Objects.requireNonNull(firstJoin, "firstJoin");
    Objects.requireNonNull(lastJoin, "lastJoin");
    ipHash = requireText(ipHash, "ipHash");
  }

  /** Returns an updated identity after a login event. */
  public PlayerIdentity joinedAs(String name, Instant joinTime, String newIpHash) {
    return new PlayerIdentity(uniqueId, name, name, firstJoin, joinTime, newIpHash);
  }

  private static String requireText(String value, String name) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return value;
  }
}
