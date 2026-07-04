package com.voxencore.player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Immutable aggregate of all supported player statistic counters. */
public final class PlayerStatistics {
  private final Map<StatisticType, Double> values;

  /** Creates statistics from values. */
  public PlayerStatistics(Map<StatisticType, Double> values) {
    EnumMap<StatisticType, Double> copy = new EnumMap<>(StatisticType.class);
    copy.putAll(values);
    this.values = Collections.unmodifiableMap(copy);
  }

  /** Returns a zeroed statistics instance. */
  public static PlayerStatistics empty() {
    return new PlayerStatistics(Map.of());
  }

  /** Returns a statistic value or zero when absent. */
  public double value(StatisticType type) {
    return values.getOrDefault(type, 0.0D);
  }

  /** Returns immutable statistic values. */
  public Map<StatisticType, Double> values() {
    return values;
  }

  /** Returns a copy with the supplied statistic increased. */
  public PlayerStatistics increment(StatisticType type, double amount) {
    EnumMap<StatisticType, Double> copy = new EnumMap<>(StatisticType.class);
    copy.putAll(values);
    copy.merge(type, amount, Double::sum);
    return new PlayerStatistics(copy);
  }
}
