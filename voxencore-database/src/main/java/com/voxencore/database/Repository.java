package com.voxencore.database;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** Asynchronous repository pattern contract for persisted entities. */
public interface Repository<ID, T> {
  /** Finds an entity by id. */
  CompletableFuture<Optional<T>> findById(ID id);

  /** Saves an entity. */
  CompletableFuture<Void> save(T entity);

  /** Deletes an entity by id. */
  CompletableFuture<Void> deleteById(ID id);
}
