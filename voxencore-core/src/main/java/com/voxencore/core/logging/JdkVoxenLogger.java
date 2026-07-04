package com.voxencore.core.logging;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/** JDK logger adapter for VoxenCore logging. */
public final class JdkVoxenLogger implements VoxenLogger {
  private final Logger logger;

  /** Creates an adapter around a JDK logger. */
  public JdkVoxenLogger(Logger logger) {
    this.logger = Objects.requireNonNull(logger, "logger");
  }

  @Override
  public void info(String message) {
    logger.info(message);
  }

  @Override
  public void warn(String message, Throwable throwable) {
    logger.log(Level.WARNING, message, throwable);
  }

  @Override
  public void error(String message, Throwable throwable) {
    logger.log(Level.SEVERE, message, throwable);
  }
}
