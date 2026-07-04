package com.voxencore.player;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;

/** Immutable player preference values such as language and timezone. */
public record PlayerPreferences(Locale language, ZoneId timezone) {
  /** Creates validated preferences. */
  public PlayerPreferences {
    Objects.requireNonNull(language, "language");
    Objects.requireNonNull(timezone, "timezone");
  }

  /** Returns default preferences for new profiles. */
  public static PlayerPreferences defaults() {
    return new PlayerPreferences(Locale.US, ZoneId.of("UTC"));
  }
}
