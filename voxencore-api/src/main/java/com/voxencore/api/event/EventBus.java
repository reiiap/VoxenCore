package com.voxencore.api.event;

import java.util.function.Consumer;

/** Type-safe in-process event bus for decoupled VoxenCore modules. */
public interface EventBus {
  /** Publishes an event to all subscribers registered for its concrete type. */
  <T> void publish(T event);

  /** Subscribes a listener for a concrete event type. */
  <T> AutoCloseable subscribe(Class<T> eventType, Consumer<? super T> listener);
}
