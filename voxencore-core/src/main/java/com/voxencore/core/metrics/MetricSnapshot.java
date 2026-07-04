package com.voxencore.core.metrics;

/** Immutable point-in-time metric value. */
public record MetricSnapshot(String name, long count, double total) {}
