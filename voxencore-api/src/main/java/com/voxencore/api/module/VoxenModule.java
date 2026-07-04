package com.voxencore.api.module;

import com.voxencore.api.service.ServiceRegistry;

/** Lifecycle contract implemented by independently deployable VoxenCore modules. */
public interface VoxenModule extends AutoCloseable {
  /** Returns the stable module identifier. */
  String id();

  /** Starts the module and registers its public services. */
  void start(ServiceRegistry services);

  /** Stops the module and releases resources. */
  @Override
  void close();
}
