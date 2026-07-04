package com.voxencore.player;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/** In-memory player session with dirty tracking and crash-recovery timestamps. */
public final class PlayerSession {
  private final UUID uniqueId;
  private final Instant startedAt;
  private final AtomicReference<PlayerProfile> profile;
  private final AtomicReference<PlayerState> state = new AtomicReference<>(PlayerState.LOADING);
  private final AtomicBoolean dirty = new AtomicBoolean();
  private final AtomicReference<Instant> lastSaveAttempt = new AtomicReference<>();

  /** Creates a session for a loaded profile. */
  public PlayerSession(PlayerProfile profile, Instant startedAt) {
    this.uniqueId = profile.uniqueId();
    this.profile = new AtomicReference<>(Objects.requireNonNull(profile, "profile"));
    this.startedAt = Objects.requireNonNull(startedAt, "startedAt");
  }

  /** Returns the player id. */
  public UUID uniqueId() {
    return uniqueId;
  }

  /** Returns when the session started. */
  public Instant startedAt() {
    return startedAt;
  }

  /** Returns the current profile. */
  public PlayerProfile profile() {
    return profile.get();
  }

  /** Replaces the profile and marks the session dirty. */
  public void update(PlayerProfile updatedProfile) {
    profile.set(Objects.requireNonNull(updatedProfile, "updatedProfile"));
    dirty.set(true);
  }

  /** Returns the current state. */
  public PlayerState state() {
    return state.get();
  }

  /** Updates the current state. */
  public void state(PlayerState state) {
    this.state.set(Objects.requireNonNull(state, "state"));
  }

  /** Returns whether this session needs saving. */
  public boolean dirty() {
    return dirty.get();
  }

  /** Marks the profile as persisted. */
  public void markSaved(Instant savedAt) {
    dirty.set(false);
    lastSaveAttempt.set(savedAt);
  }

  /** Marks that a save was attempted but not confirmed. */
  public void markSaveAttempt(Instant attemptedAt) {
    lastSaveAttempt.set(attemptedAt);
  }

  /** Returns the last save attempt time. */
  public Instant lastSaveAttempt() {
    return lastSaveAttempt.get();
  }

  /** Creates an immutable snapshot. */
  public PlayerSnapshot snapshot(Instant capturedAt) {
    return new PlayerSnapshot(profile(), state(), capturedAt);
  }

  /** Adds elapsed playtime since session start to the profile. */
  public void accumulatePlaytime(Instant now) {
    update(profile().addPlaytime(Duration.between(startedAt, now), now));
  }
}
