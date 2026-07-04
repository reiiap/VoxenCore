package com.voxencore.config;

import java.util.concurrent.CompletableFuture;

/** Asynchronous source capable of loading and saving configuration documents. */
public interface ConfigurationSource {
  /** Loads the current document. */
  CompletableFuture<ConfigurationDocument> load();

  /** Persists a document. */
  CompletableFuture<Void> save(ConfigurationDocument document);
}
