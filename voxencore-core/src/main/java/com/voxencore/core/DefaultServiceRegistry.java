package com.voxencore.core;

import com.voxencore.api.service.ServiceKey;
import com.voxencore.api.service.ServiceRegistry;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Concurrent service locator used by the plugin bootstrap and modules. */
public final class DefaultServiceRegistry implements ServiceRegistry {
  private final Map<ServiceKey<?>, Object> services = new ConcurrentHashMap<>();

  @Override
  public <T> void register(ServiceKey<T> key, T service) {
    services.put(key, key.type().cast(service));
  }

  @Override
  public <T> T require(ServiceKey<T> key) {
    return find(key).orElseThrow(() -> new IllegalStateException("Missing service: " + key.name()));
  }

  @Override
  public <T> Optional<T> find(ServiceKey<T> key) {
    return Optional.ofNullable(services.get(key)).map(key.type()::cast);
  }
}
