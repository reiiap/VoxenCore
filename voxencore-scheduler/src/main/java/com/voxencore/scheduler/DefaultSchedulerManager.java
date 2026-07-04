package com.voxencore.scheduler;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/** Executor-backed scheduler used before Paper-specific adapters are attached. */
public final class DefaultSchedulerManager implements SchedulerManager {
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

  @Override
  public CompletableFuture<Void> runAsync(TaskPriority priority, Runnable task) {
    return CompletableFuture.runAsync(task, executor);
  }

  @Override
  public AutoCloseable runDelayed(Duration delay, Runnable task) {
    ScheduledFuture<?> future = executor.schedule(task, delay.toMillis(), TimeUnit.MILLISECONDS);
    return () -> future.cancel(false);
  }

  @Override
  public AutoCloseable runRepeating(Duration initialDelay, Duration period, Runnable task) {
    ScheduledFuture<?> future = executor.scheduleAtFixedRate(
        task, initialDelay.toMillis(), period.toMillis(), TimeUnit.MILLISECONDS);
    return () -> future.cancel(false);
  }

  @Override
  public void close() {
    executor.shutdownNow();
  }
}
