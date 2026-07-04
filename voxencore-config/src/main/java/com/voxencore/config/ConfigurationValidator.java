package com.voxencore.config;

/** Validates configuration documents before they are exposed to services. */
public interface ConfigurationValidator {
  /** Validates a document or throws a configuration validation exception. */
  void validate(ConfigurationDocument document);
}
