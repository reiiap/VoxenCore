package com.voxencore.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Immutable key-value configuration document with an explicit schema version. */
public final class ConfigurationDocument {
  private final int version;
  private final Map<String, String> values;

  /** Creates a document from a version and values. */
  public ConfigurationDocument(int version, Map<String, String> values) {
    this.version = version;
    this.values = Collections.unmodifiableMap(new LinkedHashMap<>(Objects.requireNonNull(values, "values")));
  }

  /** Returns the document schema version. */
  public int version() {
    return version;
  }

  /** Returns an immutable copy of all configuration values. */
  public Map<String, String> values() {
    return values;
  }

  /** Finds a value by key. */
  public Optional<String> find(String key) {
    return Optional.ofNullable(values.get(key));
  }

  /** Returns a builder initialized from this document. */
  public Builder toBuilder() {
    return new Builder().version(version).putAll(values);
  }

  /** Creates an empty builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for immutable configuration documents. */
  public static final class Builder {
    private final Map<String, String> values = new LinkedHashMap<>();
    private int version = 1;

    /** Sets the schema version. */
    public Builder version(int version) {
      this.version = version;
      return this;
    }

    /** Adds or replaces a value. */
    public Builder put(String key, String value) {
      values.put(Objects.requireNonNull(key, "key"), Objects.requireNonNull(value, "value"));
      return this;
    }

    /** Adds all values. */
    public Builder putAll(Map<String, String> values) {
      this.values.putAll(Objects.requireNonNull(values, "values"));
      return this;
    }

    /** Builds the immutable document. */
    public ConfigurationDocument build() {
      return new ConfigurationDocument(version, values);
    }
  }
}
