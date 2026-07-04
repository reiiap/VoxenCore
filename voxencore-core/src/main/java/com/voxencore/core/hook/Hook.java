package com.voxencore.core.hook;

/** Public integration hook descriptor for optional third-party plugin APIs. */
public interface Hook {
  /** Returns the external integration name. */
  String name();

  /** Returns whether the dependency is available. */
  boolean available();

  /** Enables the hook. */
  void enable();

  /** Disables the hook. */
  void disable();
}
