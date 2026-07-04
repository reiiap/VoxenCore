package com.voxencore.scheduler;

/** Priority hint used by the central scheduler when ordering queued work. */
public enum TaskPriority {
  LOW,
  NORMAL,
  HIGH,
  CRITICAL
}
