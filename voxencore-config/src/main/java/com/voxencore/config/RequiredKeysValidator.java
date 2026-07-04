package com.voxencore.config;

import java.util.List;

/** Validator that requires a fixed set of keys to be present. */
public final class RequiredKeysValidator implements ConfigurationValidator {
  private final List<String> requiredKeys;

  /** Creates a validator for the supplied keys. */
  public RequiredKeysValidator(List<String> requiredKeys) {
    this.requiredKeys = List.copyOf(requiredKeys);
  }

  @Override
  public void validate(ConfigurationDocument document) {
    for (String key : requiredKeys) {
      if (document.find(key).isEmpty()) {
        throw new ConfigurationValidationException("Missing required configuration key: " + key);
      }
    }
  }
}
