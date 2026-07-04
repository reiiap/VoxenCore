package com.voxencore.thread;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Default fixed-pool implementation of the VoxenCore thread manager. */
public final class DefaultThreadManager implements ThreadManager {
  private final Map<ThreadPoolType, ExecutorService> executors = new EnumMap<>(ThreadPoolType.class);

  /** Creates dedicated pools sized for startup-safe baseline operation. */
  public DefaultThreadManager() {
    executors.put(ThreadPoolType.DATABASE, Executors.newFixedThreadPool(2));
    executors.put(ThreadPoolType.PACKET, Executors.newFixedThreadPool(2));
    executors.put(ThreadPoolType.GUI, Executors.newSingleThreadExecutor());
    executors.put(ThreadPoolType.FILE, Executors.newSingleThreadExecutor());
    executors.put(ThreadPoolType.BACKGROUND, Executors.newFixedThreadPool(2));
  }

  @Override
  public Executor executor(ThreadPoolType type) {
    return executors.get(type);
  }

  @Override
  public void close() {
    executors.values().forEach(ExecutorService::shutdownNow);
  }
}
