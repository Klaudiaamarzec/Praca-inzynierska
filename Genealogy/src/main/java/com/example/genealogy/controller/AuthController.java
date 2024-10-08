package com.example.genealogy.controller;

import com.example.genealogy.model.User;
import com.example.genealogy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.genealogy.service.PasswordHasher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint do rejestracji użytkownika
    @PostMapping("/Register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        if (userService.userExists(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik już istnieje!");
        }

        // Szyfrowanie hasła
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));

        // Zapis użytkownika
        boolean saved = userService.saveUser(user);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Rejestracja powiodła się!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas rejestracji.");
        }
    }

    // Endpoint do logowania
    @PostMapping("/Login")
    public ResponseEntity<String> login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        User user;

        if (usernameOrEmail.contains("@")) {
            user = userService.findByEmail(usernameOrEmail);
        } else {
            user = userService.findByUserName(usernameOrEmail);
        }

        // Sprawdź poprawność użytkownika i hasła
        if (user != null && PasswordHasher.matches(password, user.getPassword())) {
            // Ustawienia sesji, przekierowania, itp.
            return ResponseEntity.ok("Zalogowano pomyślnie!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne dane logowania!");
        }
    }

    // Endpoint do wylogowania
    @PostMapping("/Logout")
    public ResponseEntity<String> logout() {
        // Możesz dodać logikę, jeśli chcesz coś zrobić przed wylogowaniem
        return ResponseEntity.ok("Wylogowano pomyślnie!");
    }

}
