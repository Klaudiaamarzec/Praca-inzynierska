package com.example.genealogy.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.genealogy.model.User;
import com.example.genealogy.service.RoleService;
import com.example.genealogy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Base64;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthControllerTest {

    @Autowired
    private PasswordResetController passwordResetController;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUserName("newUser");
        user.setMail("newuser@example.com");
        user.setPassword("password");
        user.setIdRole(roleService.getRoleById(2));

        ResponseEntity<String> response = authController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Rejestracja powiodła się!", response.getBody());

        // Sprawdzenie, czy użytkownik został zapisany
        User savedUser = userService.findByEmail("newuser@example.com");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo("newUser");
        assertThat(savedUser.getMail()).isEqualTo("newuser@example.com");

        // Dekodowanie hasła z Base64
        String decodedPassword = new String(Base64.getDecoder().decode(savedUser.getPassword()));

        // Porównanie hasła
        assertEquals("password", decodedPassword);
    }

    @Test
    public void testRegisterUser_Conflict() {
        User existingUser = new User();
        existingUser.setUserName("iwona");
        existingUser.setMail("iwona2@onet.pl");
        existingUser.setPassword("password");
        existingUser.setIdRole(roleService.getRoleById(2));

        userService.saveUser(existingUser); // Zapisz użytkownika w bazie

        // Próba rejestracji tego samego użytkownika
        ResponseEntity<String> response = authController.registerUser(existingUser);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Użytkownik już istnieje!", response.getBody());
    }

    @Test
    public void testRegisterAndLogin_Success() {
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setMail("newuser@example.com");
        newUser.setPassword("password"); // Hasło będzie kodowane w metodzie register
        newUser.setIdRole(roleService.getRoleById(2));

        // Rejestracja użytkownika
        ResponseEntity<String> registerResponse = authController.registerUser(newUser);
        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
        assertEquals("Rejestracja powiodła się!", registerResponse.getBody());

        // Sprawdzenie, czy użytkownik został zapisany
        User savedUser = userService.findByEmail("newuser@example.com");
        assertThat(savedUser).isNotNull();

        // Dekodowanie hasła z Base64
        String decodedPassword = new String(Base64.getDecoder().decode(savedUser.getPassword()));

        // Porównanie hasła
        assertEquals("password", decodedPassword);

        // Próba logowania
        ResponseEntity<String> loginResponse = authController.login(newUser.getMail(), "password"); // Przekazanie jawnego hasła
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertEquals("Zalogowano pomyślnie!", loginResponse.getBody());
    }

    @Test
    public void testRegisterAndLogin_NotSuccess() {
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setMail("newuser@example.com");
        newUser.setPassword("password"); // Hasło będzie kodowane w metodzie register
        newUser.setIdRole(roleService.getRoleById(2));

        // Rejestracja użytkownika
        ResponseEntity<String> registerResponse = authController.registerUser(newUser);
        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
        assertEquals("Rejestracja powiodła się!", registerResponse.getBody());

        // Sprawdzenie, czy użytkownik został zapisany
        User savedUser = userService.findByEmail("newuser@example.com");
        assertThat(savedUser).isNotNull();

        // Dekodowanie hasła z Base64
        String decodedPassword = new String(Base64.getDecoder().decode(savedUser.getPassword()));

        // Porównanie hasła
        assertEquals("password", decodedPassword);

        // Próba logowania
        ResponseEntity<String> loginResponse = authController.login(newUser.getMail(), "password33"); // Przekazanie jawnego hasła
        assertEquals(HttpStatus.UNAUTHORIZED, loginResponse.getStatusCode());
        assertEquals("Niepoprawne dane logowania!", loginResponse.getBody());
    }

    @Test
    public void testRequestPasswordReset_UserExists() {
        String email = "klaudia06072002@gmail.com";

        ResponseEntity<String> response = passwordResetController.requestPasswordReset(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Link do resetowania hasła został wysłany.", response.getBody());
    }

    @Test
    public void testRequestPasswordReset_UserNotFound() {
        String email = "test@example.com";

        ResponseEntity<String> response = passwordResetController.requestPasswordReset(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Nie znaleziono użytkownika z podanym adresem e-mail.", response.getBody());
    }

    @Test
    public void testResetPassword_Success() {
        // Tworzenie nowego użytkownika
        User user = new User();
        user.setUserName("testUser");
        user.setIdRole(roleService.getRoleById(2));
        user.setMail("test@example.com");
        user.setPassword("password"); // Używamy prostego hasła, które zostanie zahardowane
        user.setResetToken("b35c84c7-c709-4933-b21a-a64ce1ed7ca3"); // Ustawienie tokenu resetowania
        user.setTokenExpirationTime(LocalDateTime.now().plusMinutes(30));

        userService.saveUser(user);

        String token = user.getResetToken();
        String newPassword = "newPassword123";

        ResponseEntity<String> response = passwordResetController.resetPassword(token, newPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hasło zostało zresetowane.", response.getBody());

        // Sprawdzenie, czy hasło zostało zaktualizowane w bazie danych
        User updatedUser = userService.findByResetToken(token);

        // Dekodowanie hasła z Base64
        String decodedPassword = new String(Base64.getDecoder().decode(updatedUser.getPassword()));

        // Porównanie hasła
        assertEquals("newPassword123", decodedPassword);
    }

    @Test
    public void testResetPassword_TokenExpired() {
        // Tworzenie nowego użytkownika
        User user = new User();
        user.setUserName("testUserExpired");
        user.setIdRole(roleService.getRoleById(2));
        user.setMail("testExpired@example.com");
        user.setPassword("password"); // Używamy prostego hasła, które zostanie zahardowane
        user.setResetToken("expired-token-123"); // Ustawienie tokenu resetowania
        // Ustawienie tokenu na wygasły (np. przed aktualnym czasem)
        user.setTokenExpirationTime(LocalDateTime.now().minusMinutes(30));

        userService.saveUser(user);

        String token = user.getResetToken();
        String newPassword = "newPassword456";

        ResponseEntity<String> response = passwordResetController.resetPassword(token, newPassword);

        // Sprawdzenie, czy odpowiedź jest odpowiednia dla wygasłego tokenu
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nieprawidłowy token.", response.getBody());

        // Porównanie hasła
        assertEquals("password", user.getPassword());
    }

}
