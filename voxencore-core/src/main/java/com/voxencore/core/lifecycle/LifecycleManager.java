package com.voxencore.core.lifecycle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/** Tracks lifecycle state and notifies observers of valid stage transitions. */
public final class LifecycleManager {
  private final AtomicReference<LifecycleStage> stage = new AtomicReference<>(LifecycleStage.CREATED);
  private final List<Consumer<LifecycleStage>> listeners = new CopyOnWriteArrayList<>();

  /** Returns the current lifecycle stage. */
  public LifecycleStage stage() {
    return stage.get();
  }

  /** Adds a stage transition listener. */
  public AutoCloseable listen(Consumer<LifecycleStage> listener) {
    listeners.add(listener);
    return () -> listeners.remove(listener);
  }

  /** Transitions to a new stage and notifies listeners. */
  public void transitionTo(LifecycleStage nextStage) {
    stage.set(nextStage);
    for (Consumer<LifecycleStage> listener : listeners) {
      listener.accept(nextStage);
    }
  }
}
