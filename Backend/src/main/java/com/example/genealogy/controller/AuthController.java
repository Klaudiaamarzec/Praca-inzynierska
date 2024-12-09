package com.example.genealogy.controller;

import com.example.genealogy.model.User;
import com.example.genealogy.service.UserService;
import com.example.genealogy.token.JwtUtil;
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
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Endpoint do rejestracji użytkownika
    @PostMapping("/Register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        if (userService.userExists(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik już istnieje!");
        }

        User existingUserName = userService.findByUserName(user.getUserName());
        User existingEmail = userService.findByEmail(user.getMail());

        if(existingUserName != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nazwa użytkownika jest już zajęta");
        }

        if(existingEmail != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Konto o podanym adresie email już istnieje");
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

            String token = jwtUtil.generateToken(user.getUserName(), user.getIdRole().getRoleName(), user.getId());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne dane logowania!");
        }
    }

}
