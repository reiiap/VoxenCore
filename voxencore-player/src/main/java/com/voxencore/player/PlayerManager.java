package com.voxencore.player;

import com.voxencore.api.event.EventBus;
import com.voxencore.player.event.PlayerCacheWarmEvent;
import com.voxencore.player.event.PlayerLoadedEvent;
import com.voxencore.player.event.PlayerSavedEvent;
import com.voxencore.player.event.PlayerSavingEvent;
import com.voxencore.player.event.PlayerUnloadEvent;
import com.voxencore.player.event.SessionEndEvent;
import com.voxencore.player.event.SessionStartEvent;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Default player service with one in-memory session per loaded player and async persistence. */
public final class PlayerManager implements PlayerService {
  private final PlayerRepository repository;
  private final PlayerCacheManager cacheManager;
  private final EventBus events;
  private final Clock clock;
  private final ConcurrentMap<UUID, PlayerSession> sessions = new ConcurrentHashMap<>();
  private final ConcurrentMap<UUID, CompletableFuture<Void>> pendingSaves = new ConcurrentHashMap<>();

  /** Creates a player manager. */
  public PlayerManager(PlayerRepository repository, PlayerCacheManager cacheManager, EventBus events, Clock clock) {
    this.repository = repository;
    this.cacheManager = cacheManager;
    this.events = events;
    this.clock = clock;
  }

  @Override
  public CompletableFuture<PlayerProfile> load(PlayerIdentity identity) {
    PlayerSession existing = sessions.get(identity.uniqueId());
    if (existing != null) {
      return CompletableFuture.completedFuture(existing.profile());
    }
    return repository.findById(identity.uniqueId()).thenCompose(stored -> {
      Instant now = clock.instant();
      PlayerProfile profile = stored
          .map(value -> value.withIdentity(value.identity().joinedAs(identity.username(), now, identity.ipHash()), now))
          .orElseGet(() -> PlayerProfile.create(identity, now));
      PlayerSession session = new PlayerSession(profile, now);
      session.state(PlayerState.ONLINE);
      PlayerSession raced = sessions.putIfAbsent(identity.uniqueId(), session);
      PlayerSession active = raced == null ? session : raced;
      events.publish(new SessionStartEvent(identity.uniqueId(), now));
      return warm(active).thenApply(ignored -> active.profile());
    });
  }

  @Override
  public Optional<PlayerProfile> online(UUID uniqueId) {
    return Optional.ofNullable(sessions.get(uniqueId)).map(PlayerSession::profile);
  }

  @Override
  public CompletableFuture<Optional<PlayerProfile>> lookup(UUID uniqueId) {
    Optional<PlayerProfile> loaded = online(uniqueId);
    if (loaded.isPresent()) {
      return CompletableFuture.completedFuture(loaded);
    }
    return repository.findById(uniqueId).thenApply(optional -> {
      optional.ifPresent(profile -> cacheManager.put(PlayerCacheType.PROFILE, uniqueId, profile));
      return optional;
    });
  }

  @Override
  public CompletableFuture<Optional<PlayerProfile>> lookup(String username) {
    return repository.findByLastKnownName(username);
  }

  @Override
  public Optional<PlayerSnapshot> snapshot(UUID uniqueId) {
    return Optional.ofNullable(sessions.get(uniqueId)).map(session -> session.snapshot(clock.instant()));
  }

  @Override
  public CompletableFuture<Void> save(UUID uniqueId) {
    PlayerSession session = sessions.get(uniqueId);
    if (session == null) {
      return CompletableFuture.completedFuture(null);
    }
    return saveSession(session);
  }

  @Override
  public CompletableFuture<Void> unload(UUID uniqueId) {
    PlayerSession session = sessions.remove(uniqueId);
    if (session == null) {
      return CompletableFuture.completedFuture(null);
    }
    Instant now = clock.instant();
    session.accumulatePlaytime(now);
    session.state(PlayerState.OFFLINE);
    return saveSession(session).thenRun(() -> {
      cacheManager.invalidate(uniqueId);
      events.publish(new SessionEndEvent(uniqueId, now));
      events.publish(new PlayerUnloadEvent(uniqueId, now));
    });
  }

  /** Saves all dirty sessions as a batch for autosave and crash recovery checkpoints. */
  public CompletableFuture<Void> saveDirtySessions() {
    Collection<PlayerProfile> dirtyProfiles = new ArrayList<>();
    Instant now = clock.instant();
    for (PlayerSession session : sessions.values()) {
      if (session.dirty()) {
        session.markSaveAttempt(now);
        dirtyProfiles.add(session.profile());
      }
    }
    if (dirtyProfiles.isEmpty()) {
      return CompletableFuture.completedFuture(null);
    }
    dirtyProfiles.forEach(profile -> events.publish(new PlayerSavingEvent(profile.uniqueId(), now)));
    return repository.saveAll(dirtyProfiles).thenRun(() -> {
      Instant savedAt = clock.instant();
      for (PlayerProfile profile : dirtyProfiles) {
        PlayerSession session = sessions.get(profile.uniqueId());
        if (session != null) {
          session.markSaved(savedAt);
        }
        events.publish(new PlayerSavedEvent(profile.uniqueId(), savedAt));
      }
    });
  }

  private CompletableFuture<Void> warm(PlayerSession session) {
    Instant now = clock.instant();
    events.publish(new PlayerCacheWarmEvent(session.uniqueId(), now));
    return cacheManager.preload(session.profile()).thenRun(() -> events.publish(new PlayerLoadedEvent(session.uniqueId(), now)));
  }

  private CompletableFuture<Void> saveSession(PlayerSession session) {
    Instant now = clock.instant();
    session.state(PlayerState.SAVING);
    session.markSaveAttempt(now);
    events.publish(new PlayerSavingEvent(session.uniqueId(), now));
    return pendingSaves.compute(session.uniqueId(), (uniqueId, previous) -> {
      CompletableFuture<Void> base = previous == null ? CompletableFuture.completedFuture(null) : previous;
      return base.thenCompose(ignored -> repository.save(session.profile())).thenRun(() -> {
        Instant savedAt = clock.instant();
        session.markSaved(savedAt);
        if (sessions.containsKey(session.uniqueId())) {
          session.state(PlayerState.ONLINE);
        }
        events.publish(new PlayerSavedEvent(session.uniqueId(), savedAt));
      });
    }).whenComplete((ignored, throwable) -> pendingSaves.remove(session.uniqueId()));
  }
}
