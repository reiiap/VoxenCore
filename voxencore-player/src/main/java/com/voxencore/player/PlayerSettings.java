package com.voxencore.player;

/** Immutable player platform settings toggles. */
public record PlayerSettings(boolean privateMessagesEnabled, boolean scoreboardEnabled, boolean tabEnabled) {
  /** Returns default settings for new profiles. */
  public static PlayerSettings defaults() {
    return new PlayerSettings(true, true, true);
  }
}
