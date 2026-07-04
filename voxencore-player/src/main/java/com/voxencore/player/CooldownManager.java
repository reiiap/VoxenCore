package com.voxencore.player;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Universal thread-safe player cooldown manager. */
public final class CooldownManager {
  private final Map<UUID, Map<CooldownCategory, Instant>> cooldowns = new ConcurrentHashMap<>();
  private final Clock clock;

  /** Creates a cooldown manager using the system UTC clock. */
  public CooldownManager() {
    this(Clock.systemUTC());
  }

  /** Creates a cooldown manager using an explicit clock. */
  public CooldownManager(Clock clock) {
    this.clock = clock;
  }

  /** Sets a cooldown for a player and category. */
  public void setCooldown(UUID uniqueId, CooldownCategory category, Duration duration) {
    cooldowns.computeIfAbsent(uniqueId, ignored -> new EnumMap<>(CooldownCategory.class))
        .put(category, clock.instant().plus(duration));
  }

  /** Returns whether the player currently has a non-expired cooldown. */
  public boolean hasCooldown(UUID uniqueId, CooldownCategory category) {
    return remaining(uniqueId, category).compareTo(Duration.ZERO) > 0;
  }

  /** Returns remaining cooldown duration or zero when absent or expired. */
  public Duration remaining(UUID uniqueId, CooldownCategory category) {
    Map<CooldownCategory, Instant> playerCooldowns = cooldowns.get(uniqueId);
    if (playerCooldowns == null) {
      return Duration.ZERO;
    }
    Instant expiresAt = playerCooldowns.get(category);
    if (expiresAt == null) {
      return Duration.ZERO;
    }
    Duration remaining = Duration.between(clock.instant(), expiresAt);
    if (!remaining.isPositive()) {
      playerCooldowns.remove(category);
      return Duration.ZERO;
    }
    return remaining;
  }

  /** Clears a cooldown for a player and category. */
  public void clear(UUID uniqueId, CooldownCategory category) {
    Map<CooldownCategory, Instant> playerCooldowns = cooldowns.get(uniqueId);
    if (playerCooldowns != null) {
      playerCooldowns.remove(category);
    }
  }
}
