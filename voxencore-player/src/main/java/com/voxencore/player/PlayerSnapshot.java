package com.voxencore.player;

import java.time.Instant;
import java.util.Objects;

/** Immutable read-only snapshot of a player session and profile. */
public record PlayerSnapshot(PlayerProfile profile, PlayerState state, Instant capturedAt) {
  /** Creates a validated snapshot. */
  public PlayerSnapshot {
    Objects.requireNonNull(profile, "profile");
    Objects.requireNonNull(state, "state");
    Objects.requireNonNull(capturedAt, "capturedAt");
  }
}
