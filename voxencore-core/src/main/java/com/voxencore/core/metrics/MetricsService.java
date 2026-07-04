package com.voxencore.core.metrics;

import java.util.List;

/** Records lightweight runtime metrics for VoxenCore subsystems. */
public interface MetricsService {
  /** Increments a counter by one. */
  void increment(String name);

  /** Records a numeric measurement. */
  void record(String name, double value);

  /** Returns immutable metric snapshots. */
  List<MetricSnapshot> snapshots();
}
