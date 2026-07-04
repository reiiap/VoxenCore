package com.voxencore.core;

import com.voxencore.api.event.EventBus;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/** Lock-light event bus optimized for module-local lifecycle events. */
public final class SimpleEventBus implements EventBus {
  private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

  @Override
  public <T> void publish(T event) {
    List<Consumer<?>> consumers = listeners.getOrDefault(event.getClass(), List.of());
    for (Consumer<?> consumer : consumers) {
      deliver(consumer, event);
    }
  }

  @Override
  public <T> AutoCloseable subscribe(Class<T> eventType, Consumer<? super T> listener) {
    listeners.computeIfAbsent(eventType, ignored -> new CopyOnWriteArrayList<>()).add(listener);
    return () -> listeners.getOrDefault(eventType, List.of()).remove(listener);
  }

  private static <T> void deliver(Consumer<?> consumer, T event) {
    @SuppressWarnings("unchecked")
    Consumer<T> typed = (Consumer<T>) consumer;
    typed.accept(event);
  }
}
