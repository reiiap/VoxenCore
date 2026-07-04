package com.voxencore.core.logging;

/** Structured logging contract used by VoxenCore infrastructure services. */
public interface VoxenLogger {
  /** Logs informational operational state. */
  void info(String message);

  /** Logs recoverable warnings. */
  void warn(String message, Throwable throwable);

  /** Logs unrecoverable subsystem failures. */
  void error(String message, Throwable throwable);
}
