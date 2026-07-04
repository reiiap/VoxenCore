package com.voxencore.player;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Immutable string metadata bag for player-scoped framework attributes. */
public final class PlayerMetadata {
  private final Map<String, String> values;

  /** Creates immutable metadata from values. */
  public PlayerMetadata(Map<String, String> values) {
    this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
  }

  /** Returns an empty metadata instance. */
  public static PlayerMetadata empty() {
    return new PlayerMetadata(Map.of());
  }

  /** Finds metadata by key. */
  public Optional<String> find(String key) {
    return Optional.ofNullable(values.get(key));
  }

  /** Returns immutable metadata values. */
  public Map<String, String> values() {
    return values;
  }

  /** Returns a new metadata instance with a changed value. */
  public PlayerMetadata with(String key, String value) {
    Map<String, String> copy = new LinkedHashMap<>(values);
    copy.put(key, value);
    return new PlayerMetadata(copy);
  }
}
