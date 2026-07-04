package com.voxencore.player;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/** Coordinates autosave, disconnect save, and crash-recovery checkpoints for player sessions. */
public final class PlayerSessionManager implements AutoCloseable {
  private final PlayerManager playerManager;
  private final ScheduledFuture<?> autosaveTask;

  /** Creates a session manager and starts the autosave scheduler. */
  public PlayerSessionManager(
      PlayerManager playerManager, ScheduledExecutorService scheduler, Duration autosaveInterval) {
    this.playerManager = Objects.requireNonNull(playerManager, "playerManager");
    Objects.requireNonNull(scheduler, "scheduler");
    Objects.requireNonNull(autosaveInterval, "autosaveInterval");
    long intervalMillis = Math.max(1L, autosaveInterval.toMillis());
    autosaveTask = scheduler.scheduleAtFixedRate(
        () -> this.playerManager.saveDirtySessions(), intervalMillis, intervalMillis, TimeUnit.MILLISECONDS);
  }

  /** Saves and unloads a disconnecting player asynchronously. */
  public CompletableFuture<Void> disconnect(UUID uniqueId) {
    return playerManager.unload(uniqueId);
  }

  /** Forces a crash-recovery checkpoint for all dirty sessions. */
  public CompletableFuture<Void> checkpoint() {
    return playerManager.saveDirtySessions();
  }

  @Override
  public void close() {
    autosaveTask.cancel(false);
  }
}
