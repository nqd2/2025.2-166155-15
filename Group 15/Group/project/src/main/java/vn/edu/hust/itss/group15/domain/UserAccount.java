package vn.edu.hust.itss.group15.domain;

import java.util.Set;

public record UserAccount(
    String userId,
    String username,
    String email,
    UserStatus status,
    Set<String> actorRoles,
    String passwordHash,
    String passwordSalt) {

  public UserAccount(String userId, String username, String email, UserStatus status, Set<String> actorRoles) {
    this(userId, username, email, status, actorRoles, "", "");
  }

  public UserAccount {
    if (userId == null || userId.isBlank()) {
      throw new IllegalArgumentException("userId is required");
    }
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username is required");
    }
    if (email == null || !email.contains("@")) {
      throw new IllegalArgumentException("email must be valid");
    }
    if (actorRoles == null || actorRoles.isEmpty()) {
      throw new IllegalArgumentException("actorRole is required");
    }
    actorRoles = Set.copyOf(actorRoles);
    passwordHash = passwordHash == null ? "" : passwordHash;
    passwordSalt = passwordSalt == null ? "" : passwordSalt;
  }
}
