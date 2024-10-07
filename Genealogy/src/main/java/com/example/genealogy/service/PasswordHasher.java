package com.example.genealogy.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class PasswordHasher {

    public static String hashPassword(String password) {
        // Konwertuj hasło na bajty i koduj do Base64
        byte[] encodedBytes = Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        // Porównaj hasło w postaci surowej z zakodowanym hasłem
        String encodedRawPassword = hashPassword(rawPassword);
        return encodedRawPassword.equals(hashedPassword);
    }
}
