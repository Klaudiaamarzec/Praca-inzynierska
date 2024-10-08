package com.example.genealogy.controller;

import com.example.genealogy.model.User;
import com.example.genealogy.service.PasswordHasher;
import com.example.genealogy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/Auth")
public class PasswordResetController {

    private final UserService userService;

    @Autowired
    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint do żądania resetowania hasła
    @PostMapping("/ResetPassword/Request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {

        // Sprawdzenie, czy e-mail istnieje w bazie
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika z podanym adresem e-mail.");
        }

        // Generowanie tokenu i wysyłanie e-maila
        String token = userService.createPasswordResetToken(user);
        userService.sendResetPasswordEmail(user.getMail(), token);

        return ResponseEntity.ok("Link do resetowania hasła został wysłany.");
    }

    // Endpoint do resetowania hasła
    @PostMapping("/ResetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {

        // Znajdź użytkownika na podstawie tokena
        User user = userService.findByResetToken(token);

        // Sprawdź, czy użytkownik istnieje i czy token jest ważny
        if (user == null || !isTokenValid(user, token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nieprawidłowy token.");
        }

        // Zaktualizuj hasło użytkownika
        user.setPassword(PasswordHasher.hashPassword(newPassword));

        if (userService.updateUser(user)) {
            return ResponseEntity.ok("Hasło zostało zresetowane.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie udało się zaktualizować hasła.");
        }
    }

    private boolean isTokenValid(User user, String token) {
        // Sprawdzanie, czy token jest zgodny
        if (!user.getResetToken().equals(token)) {
            return false;
        }

        // Sprawdzanie, czy token nie wygasł
        LocalDateTime tokenExpirationTime = user.getTokenExpirationTime();
        return tokenExpirationTime != null && LocalDateTime.now().isBefore(tokenExpirationTime);
    }
}
