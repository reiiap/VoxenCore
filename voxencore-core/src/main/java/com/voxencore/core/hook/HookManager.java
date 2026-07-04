package com.voxencore.core.hook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Registers and coordinates optional plugin integration hooks. */
public final class HookManager implements AutoCloseable {
  private final List<Hook> hooks = new ArrayList<>();

  /** Registers a hook and enables it when its dependency is available. */
  public void register(Hook hook) {
    hooks.add(hook);
    if (hook.available()) {
      hook.enable();
    }
  }

  /** Finds a registered hook by name. */
  public Optional<Hook> find(String name) {
    return hooks.stream().filter(hook -> hook.name().equalsIgnoreCase(name)).findFirst();
  }

  /** Returns registered hook names. */
  public List<String> names() {
    return hooks.stream().map(Hook::name).toList();
  }

  @Override
  public void close() {
    for (int index = hooks.size() - 1; index >= 0; index--) {
      Hook hook = hooks.get(index);
      if (hook.available()) {
        hook.disable();
      }
    }
    hooks.clear();
  }
}
