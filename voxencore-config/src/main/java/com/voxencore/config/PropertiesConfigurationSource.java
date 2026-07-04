package com.voxencore.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Properties-file-backed configuration source suitable for deterministic tests and small configs. */
public final class PropertiesConfigurationSource implements ConfigurationSource {
  private static final String VERSION_KEY = "config.version";
  private final Path path;
  private final Executor executor;

  /** Creates a source for a properties file. */
  public PropertiesConfigurationSource(Path path, Executor executor) {
    this.path = path;
    this.executor = executor;
  }

  @Override
  public CompletableFuture<ConfigurationDocument> load() {
    return CompletableFuture.supplyAsync(this::loadBlocking, executor);
  }

  @Override
  public CompletableFuture<Void> save(ConfigurationDocument document) {
    return CompletableFuture.runAsync(() -> saveBlocking(document), executor);
  }

  private ConfigurationDocument loadBlocking() {
    Properties properties = new Properties();
    if (Files.exists(path)) {
      try (Reader reader = Files.newBufferedReader(path)) {
        properties.load(reader);
      } catch (IOException exception) {
        throw new IllegalStateException("Unable to load configuration " + path, exception);
      }
    }
    int version = Integer.parseInt(properties.getProperty(VERSION_KEY, "1"));
    Map<String, String> values = new LinkedHashMap<>();
    for (String name : properties.stringPropertyNames()) {
      if (!VERSION_KEY.equals(name)) {
        values.put(name, properties.getProperty(name));
      }
    }
    return new ConfigurationDocument(version, values);
  }

  private void saveBlocking(ConfigurationDocument document) {
    Properties properties = new Properties();
    properties.setProperty(VERSION_KEY, Integer.toString(document.version()));
    document.values().forEach(properties::setProperty);
    try {
      Path parent = path.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      try (Writer writer = Files.newBufferedWriter(path)) {
        properties.store(writer, "VoxenCore configuration");
      }
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to save configuration " + path, exception);
    }
  }
}
