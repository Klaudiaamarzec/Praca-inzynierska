package com.example.genealogy.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "BardzoBezpiecznyKlucz98JeszczeBardziejBezpieczny";

    // Generowanie tokenu JWT
    public String generateToken(String username, String role, Long userId) {

        SecretKey key = createKey();

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3)) // 3 godziny
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey createKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    // Wyodrębnianie daty wygaśnięcia tokenu
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder() // Użyj parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Wyodrębnianie Claims z tokenu
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder() // Użyj parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Wyodrębnianie nazwy użytkownika z tokenu
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Wyodrębnianie roli z tokenu
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Wyodrębnianie id użytkownika z tokenu
    public Long extractUserId(String token) {
        return extractClaims(token).get("id", Long.class);
    }

    // Sprawdzanie, czy token jest wygasły
    public boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    // Weryfikacja tokenu
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && isTokenExpired(token);
    }
}
