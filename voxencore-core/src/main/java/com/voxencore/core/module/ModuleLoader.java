package com.voxencore.core.module;

import com.voxencore.api.module.VoxenModule;
import com.voxencore.api.service.ServiceRegistry;
import java.util.ArrayList;
import java.util.List;

/** Loads and unloads VoxenCore modules in deterministic registration order. */
public final class ModuleLoader implements AutoCloseable {
  private final ServiceRegistry services;
  private final List<VoxenModule> loadedModules = new ArrayList<>();

  /** Creates a module loader using the shared service registry. */
  public ModuleLoader(ServiceRegistry services) {
    this.services = services;
  }

  /** Starts a module and tracks it for reverse-order shutdown. */
  public void load(VoxenModule module) {
    module.start(services);
    loadedModules.add(module);
  }

  /** Returns loaded module identifiers. */
  public List<String> loadedModuleIds() {
    return loadedModules.stream().map(VoxenModule::id).toList();
  }

  @Override
  public void close() {
    for (int index = loadedModules.size() - 1; index >= 0; index--) {
      loadedModules.get(index).close();
    }
    loadedModules.clear();
  }
}
