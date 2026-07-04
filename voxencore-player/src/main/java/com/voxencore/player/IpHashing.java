package com.voxencore.player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

/** Hashing utility that prevents plaintext IP address persistence. */
public final class IpHashing {
  private IpHashing() {}

  /** Produces a SHA-256 hash from an address and server-side salt. */
  public static String sha256(String address, String salt) {
    Objects.requireNonNull(address, "address");
    Objects.requireNonNull(salt, "salt");
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(salt.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest.digest(address.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 is not available", exception);
    }
  }
}
