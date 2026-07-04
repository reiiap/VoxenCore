package org.bukkit.plugin.java;

import java.util.logging.Logger;

/** Compile-time Paper API stub used only when external repositories are unavailable. */
public abstract class JavaPlugin {
  private final Logger logger = Logger.getLogger(getClass().getName());

  /** Called by Paper when the plugin is enabled. */
  public void onEnable() {}

  /** Called by Paper when the plugin is disabled. */
  public void onDisable() {}

  /** Returns the plugin logger. */
  public Logger getLogger() {
    return logger;
  }
}
