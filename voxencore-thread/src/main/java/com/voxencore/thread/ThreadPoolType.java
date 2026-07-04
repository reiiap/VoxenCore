package com.voxencore.thread;

/** Dedicated executor categories used to isolate high-cost VoxenCore workloads. */
public enum ThreadPoolType {
  DATABASE,
  PACKET,
  GUI,
  FILE,
  BACKGROUND
}
