package com.voxencore.core.metrics;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/** Lock-free in-memory metrics service for counters and aggregate measurements. */
public final class InMemoryMetricsService implements MetricsService {
  private final Map<String, MetricValue> values = new ConcurrentHashMap<>();

  @Override
  public void increment(String name) {
    record(name, 1.0D);
  }

  @Override
  public void record(String name, double value) {
    MetricValue metricValue = values.computeIfAbsent(name, ignored -> new MetricValue());
    metricValue.count.increment();
    metricValue.total.add(value);
  }

  @Override
  public List<MetricSnapshot> snapshots() {
    return values.entrySet().stream()
        .map(entry -> new MetricSnapshot(entry.getKey(), entry.getValue().count.sum(), entry.getValue().total.sum()))
        .sorted(Comparator.comparing(MetricSnapshot::name))
        .toList();
  }

  private static final class MetricValue {
    private final LongAdder count = new LongAdder();
    private final DoubleAdder total = new DoubleAdder();
  }
}
