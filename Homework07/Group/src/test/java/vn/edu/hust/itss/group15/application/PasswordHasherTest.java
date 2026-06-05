package vn.edu.hust.itss.group15.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

  @Test
  void hashAllowsValidPasswords() {
    // Exactly 8 characters (boundary)
    PasswordHasher.PasswordCredential cred8 = PasswordHasher.hash("12345678");
    assertNotNull(cred8.hash());
    assertNotNull(cred8.salt());
    assertTrue(PasswordHasher.matches("12345678", cred8.hash(), cred8.salt()));

    // Very long password
    String longPassword = "a".repeat(100);
    PasswordHasher.PasswordCredential credLong = PasswordHasher.hash(longPassword);
    assertNotNull(credLong.hash());
    assertTrue(PasswordHasher.matches(longPassword, credLong.hash(), credLong.salt()));
  }

  @Test
  void hashRejectsShortOrNullPasswords() {
    // 7 characters (boundary fail)
    BusinessException ex1 = assertThrows(BusinessException.class, () -> PasswordHasher.hash("1234567"));
    assertEquals("password must contain at least 8 characters", ex1.getMessage());

    // Null password
    BusinessException ex2 = assertThrows(BusinessException.class, () -> PasswordHasher.hash(null));
    assertEquals("password must contain at least 8 characters", ex2.getMessage());
  }

  @Test
  void matchesHandlesInvalidInputs() {
    PasswordHasher.PasswordCredential cred = PasswordHasher.hash("12345678");

    // Wrong password
    assertFalse(PasswordHasher.matches("wrongpass", cred.hash(), cred.salt()));

    // Null/blank parameters
    assertFalse(PasswordHasher.matches(null, cred.hash(), cred.salt()));
    assertFalse(PasswordHasher.matches("12345678", null, cred.salt()));
    assertFalse(PasswordHasher.matches("12345678", "", cred.salt()));
    assertFalse(PasswordHasher.matches("12345678", cred.hash(), null));
    assertFalse(PasswordHasher.matches("12345678", cred.hash(), ""));
  }
}
