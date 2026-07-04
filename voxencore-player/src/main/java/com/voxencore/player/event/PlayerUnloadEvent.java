package com.voxencore.player.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Framework event emitted by the VoxenCore player platform. */
public record PlayerUnloadEvent(UUID uniqueId, Instant occurredAt) {
  /** Creates a validated player event. */
  public PlayerUnloadEvent {
    Objects.requireNonNull(uniqueId, "uniqueId");
    Objects.requireNonNull(occurredAt, "occurredAt");
  }
}
