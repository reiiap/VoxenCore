package com.voxencore.config;

/** Indicates that a configuration document violates its schema constraints. */
public final class ConfigurationValidationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /** Creates a validation exception with a human-readable message. */
  public ConfigurationValidationException(String message) {
    super(message);
  }
}
