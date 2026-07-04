package com.voxencore.scheduler;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/** Central scheduler abstraction for delayed, repeating, synchronous, and asynchronous work. */
public interface SchedulerManager extends AutoCloseable {
  /** Runs work asynchronously using the supplied priority hint. */
  CompletableFuture<Void> runAsync(TaskPriority priority, Runnable task);

  /** Runs work after the supplied delay. */
  AutoCloseable runDelayed(Duration delay, Runnable task);

  /** Runs work repeatedly until the returned handle is closed. */
  AutoCloseable runRepeating(Duration initialDelay, Duration period, Runnable task);

  /** Stops scheduled work owned by this manager. */
  @Override
  void close();
}
