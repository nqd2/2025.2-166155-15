package vn.edu.hust.itss.group15.application;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class PasswordHasher {
  private static final int ITERATIONS = 120_000;
  private static final int KEY_LENGTH = 256;
  private static final SecureRandom RANDOM = new SecureRandom();

  private PasswordHasher() {}

  static PasswordCredential hash(String password) {
    if (password == null || password.length() < 8) {
      throw new BusinessException("password must contain at least 8 characters");
    }
    byte[] salt = new byte[16];
    RANDOM.nextBytes(salt);
    return new PasswordCredential(hash(password.toCharArray(), salt), Base64.getEncoder().encodeToString(salt));
  }

  static boolean matches(String password, String expectedHash, String salt) {
    if (password == null || expectedHash == null || expectedHash.isBlank() || salt == null || salt.isBlank()) {
      return false;
    }
    byte[] decodedSalt = Base64.getDecoder().decode(salt);
    return hash(password.toCharArray(), decodedSalt).equals(expectedHash);
  }

  private static String hash(char[] password, byte[] salt) {
    try {
      KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
    } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
      throw new IllegalStateException("password hashing failed", exception);
    }
  }

  record PasswordCredential(String hash, String salt) {}
}
