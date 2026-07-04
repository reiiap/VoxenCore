package com.voxencore.core;

import com.voxencore.api.event.EventBus;
import com.voxencore.api.service.ServiceKey;
import com.voxencore.api.service.ServiceRegistry;
import com.voxencore.scheduler.DefaultSchedulerManager;
import com.voxencore.scheduler.SchedulerManager;
import com.voxencore.thread.DefaultThreadManager;
import com.voxencore.thread.ThreadManager;
import org.bukkit.plugin.java.JavaPlugin;

/** Paper entry point for the VoxenCore enterprise framework. */
public final class VoxenCorePlugin extends JavaPlugin {
  private final DefaultServiceRegistry services = new DefaultServiceRegistry();
  private ThreadManager threadManager;
  private SchedulerManager schedulerManager;

  @Override
  public void onEnable() {
    threadManager = new DefaultThreadManager();
    schedulerManager = new DefaultSchedulerManager();
    services.register(ServiceKey.of(ServiceRegistry.class), services);
    services.register(ServiceKey.of(EventBus.class), new SimpleEventBus());
    services.register(ServiceKey.of(ThreadManager.class), threadManager);
    services.register(ServiceKey.of(SchedulerManager.class), schedulerManager);
    getLogger().info("VoxenCore framework services are online.");
  }

  @Override
  public void onDisable() {
    if (schedulerManager != null) {
      schedulerManager.close();
    }
    if (threadManager != null) {
      threadManager.close();
    }
    getLogger().info("VoxenCore framework services are offline.");
  }

  /** Returns the root service registry for integration modules. */
  public ServiceRegistry services() {
    return services;
  }
}
