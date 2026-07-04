package com.voxencore.player;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Immutable aggregate root for persisted player data. */
public record PlayerProfile(
    PlayerIdentity identity,
    PlayerPreferences preferences,
    PlayerStatistics statistics,
    PlayerMetadata metadata,
    PlayerSettings settings,
    Duration playtime,
    Instant updatedAt) {
  /** Creates a validated player profile. */
  public PlayerProfile {
    Objects.requireNonNull(identity, "identity");
    Objects.requireNonNull(preferences, "preferences");
    Objects.requireNonNull(statistics, "statistics");
    Objects.requireNonNull(metadata, "metadata");
    Objects.requireNonNull(settings, "settings");
    Objects.requireNonNull(playtime, "playtime");
    Objects.requireNonNull(updatedAt, "updatedAt");
  }

  /** Creates a profile for a first-time player. */
  public static PlayerProfile create(PlayerIdentity identity, Instant now) {
    return new PlayerProfile(
        identity,
        PlayerPreferences.defaults(),
        PlayerStatistics.empty(),
        PlayerMetadata.empty(),
        PlayerSettings.defaults(),
        Duration.ZERO,
        now);
  }

  /** Returns the unique player id. */
  public UUID uniqueId() {
    return identity.uniqueId();
  }

  /** Returns a profile with new preferences. */
  public PlayerProfile withPreferences(PlayerPreferences preferences, Instant now) {
    return new PlayerProfile(identity, preferences, statistics, metadata, settings, playtime, now);
  }

  /** Returns a profile with new statistics. */
  public PlayerProfile withStatistics(PlayerStatistics statistics, Instant now) {
    return new PlayerProfile(identity, preferences, statistics, metadata, settings, playtime, now);
  }

  /** Returns a profile with new metadata. */
  public PlayerProfile withMetadata(PlayerMetadata metadata, Instant now) {
    return new PlayerProfile(identity, preferences, statistics, metadata, settings, playtime, now);
  }

  /** Returns a profile with new settings. */
  public PlayerProfile withSettings(PlayerSettings settings, Instant now) {
    return new PlayerProfile(identity, preferences, statistics, metadata, settings, playtime, now);
  }

  /** Returns a profile with an updated identity. */
  public PlayerProfile withIdentity(PlayerIdentity identity, Instant now) {
    return new PlayerProfile(identity, preferences, statistics, metadata, settings, playtime, now);
  }

  /** Returns a profile with accumulated playtime. */
  public PlayerProfile addPlaytime(Duration duration, Instant now) {
    return new PlayerProfile(identity, preferences, statistics, metadata, settings, playtime.plus(duration), now);
  }
}
