package com.voxencore.thread;

import java.util.concurrent.Executor;

/** Provides dedicated executors for isolated subsystem workloads. */
public interface ThreadManager extends AutoCloseable {
  /** Returns the executor assigned to the requested workload type. */
  Executor executor(ThreadPoolType type);

  /** Stops all owned executors. */
  @Override
  void close();
}
