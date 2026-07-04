package com.voxencore.core;

import com.voxencore.api.event.EventBus;
import com.voxencore.api.service.ServiceKey;
import com.voxencore.api.service.ServiceRegistry;
import com.voxencore.core.hook.HookManager;
import com.voxencore.core.lifecycle.LifecycleManager;
import com.voxencore.core.lifecycle.LifecycleStage;
import com.voxencore.core.logging.JdkVoxenLogger;
import com.voxencore.core.logging.VoxenLogger;
import com.voxencore.core.metrics.InMemoryMetricsService;
import com.voxencore.core.metrics.MetricsService;
import com.voxencore.core.module.ModuleLoader;
import com.voxencore.player.CooldownManager;
import com.voxencore.player.InMemoryPlayerRepository;
import com.voxencore.player.PlayerCacheManager;
import com.voxencore.player.PlayerManager;
import com.voxencore.player.PlayerService;
import com.voxencore.scheduler.DefaultSchedulerManager;
import com.voxencore.scheduler.SchedulerManager;
import com.voxencore.thread.DefaultThreadManager;
import com.voxencore.thread.ThreadManager;
import java.time.Clock;
import java.time.Duration;
import org.bukkit.plugin.java.JavaPlugin;

/** Paper entry point for the VoxenCore enterprise framework. */
public final class VoxenCorePlugin extends JavaPlugin {
  private final DefaultServiceRegistry services = new DefaultServiceRegistry();
  private ThreadManager threadManager;
  private SchedulerManager schedulerManager;
  private HookManager hookManager;
  private ModuleLoader moduleLoader;
  private LifecycleManager lifecycleManager;
  private PlayerManager playerManager;

  @Override
  public void onEnable() {
    lifecycleManager = new LifecycleManager();
    lifecycleManager.transitionTo(LifecycleStage.STARTING);
    threadManager = new DefaultThreadManager();
    schedulerManager = new DefaultSchedulerManager();
    hookManager = new HookManager();
    moduleLoader = new ModuleLoader(services);
    EventBus eventBus = new SimpleEventBus();
    playerManager = new PlayerManager(
        new InMemoryPlayerRepository(),
        new PlayerCacheManager(Duration.ofMinutes(30L)),
        eventBus,
        Clock.systemUTC());
    services.register(ServiceKey.of(ServiceRegistry.class), services);
    services.register(ServiceKey.of(EventBus.class), eventBus);
    services.register(ServiceKey.of(ThreadManager.class), threadManager);
    services.register(ServiceKey.of(SchedulerManager.class), schedulerManager);
    services.register(ServiceKey.of(VoxenLogger.class), new JdkVoxenLogger(getLogger()));
    services.register(ServiceKey.of(MetricsService.class), new InMemoryMetricsService());
    services.register(ServiceKey.of(HookManager.class), hookManager);
    services.register(ServiceKey.of(ModuleLoader.class), moduleLoader);
    services.register(ServiceKey.of(LifecycleManager.class), lifecycleManager);
    services.register(ServiceKey.of(PlayerService.class), playerManager);
    services.register(ServiceKey.of(PlayerManager.class), playerManager);
    services.register(ServiceKey.of(CooldownManager.class), new CooldownManager());
    lifecycleManager.transitionTo(LifecycleStage.RUNNING);
    getLogger().info("VoxenCore framework services are online.");
  }

  @Override
  public void onDisable() {
    if (lifecycleManager != null) {
      lifecycleManager.transitionTo(LifecycleStage.STOPPING);
    }
    if (moduleLoader != null) {
      moduleLoader.close();
    }
    if (hookManager != null) {
      hookManager.close();
    }
    if (schedulerManager != null) {
      schedulerManager.close();
    }
    if (threadManager != null) {
      threadManager.close();
    }
    if (lifecycleManager != null) {
      lifecycleManager.transitionTo(LifecycleStage.STOPPED);
    }
    getLogger().info("VoxenCore framework services are offline.");
  }

  /** Returns the root service registry for integration modules. */
  public ServiceRegistry services() {
    return services;
  }
}
