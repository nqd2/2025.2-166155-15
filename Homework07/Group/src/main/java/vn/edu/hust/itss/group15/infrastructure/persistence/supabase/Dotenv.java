package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Dotenv {
  private static final Map<String, String> envMap = new HashMap<>();

  static {
    load();
  }

  private Dotenv() {}

  public static void load() {
    File envFile = findDotenvFile();
    if (envFile == null || !envFile.exists()) {
      return;
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }
        int eqIndex = line.indexOf('=');
        if (eqIndex > 0) {
          String key = line.substring(0, eqIndex).trim();
          String value = line.substring(eqIndex + 1).trim();
          if (!value.isEmpty()) {
            if ((value.startsWith("\"") && value.endsWith("\"")) || 
                (value.startsWith("'") && value.endsWith("'"))) {
              value = value.substring(1, value.length() - 1);
            } else {
              int hashIndex = value.indexOf('#');
              if (hashIndex >= 0) {
                value = value.substring(0, hashIndex).trim();
              }
            }
          }
          envMap.put(key, value);
        }
      }
    } catch (IOException e) {
      System.err.println("Warning: failed to read .env file: " + e.getMessage());
    }
  }

  private static File findDotenvFile() {
    File file = new File(".env");
    if (file.exists()) {
      return file;
    }
    file = new File("../.env");
    if (file.exists()) {
      return file;
    }
    file = new File("Homework07/Group/.env");
    if (file.exists()) {
      return file;
    }
    return null;
  }

  public static String get(String key) {
    String val = envMap.get(key);
    if (val != null) {
      return val;
    }
    return System.getenv(key);
  }

  public static String get(String key, String defaultValue) {
    String val = get(key);
    return val != null ? val : defaultValue;
  }
}
