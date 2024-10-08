package com.example.genealogy.service;

import com.example.genealogy.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        // Inicjalizacja mocków
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsById() {
        boolean exists = userService.existsById(1L);
        assertThat(exists).isTrue();
    }

    @Test
    void testUserExists() {

        User user = new User();
        user.setIdRole(roleService.getRoleById(2));
        user.setUserName("iwona");
        user.setMail("iwona@onet.pl");

        boolean result = userService.userExists(user);
        assertThat(result).isTrue();
    }

    @Test
    void testUserNotExists() {

        User user = new User();
        user.setIdRole(roleService.getRoleById(2));
        user.setUserName("klaudia");
        user.setMail("iwona@onet.pl");

        boolean result = userService.userExists(user);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setIdRole(roleService.getRoleById(1));
        user.setUserName("john_doe");
        user.setPassword("secure_password");
        user.setMail("john.doe@example.com");

        boolean isSaved = userService.saveUser(user);

        assertThat(isSaved).isTrue();
        assertThat(userService.getAllUsers()).contains(user);
    }

    @Test
    void testNotSaveUserWithEmptyUserName() {
        User user = new User();
        user.setIdRole(roleService.getRoleById(1));
        user.setUserName(null);
        user.setPassword("secure_password");
        user.setMail("john.doe@example.com");

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> userService.saveUser(user));

        assertThat(thrown.getConstraintViolations()).hasSize(1);

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Nazwa użytkownika nie może być pusta");
    }

    @Test
    void testNotSaveUser() {
        User user = new User();
        user.setIdRole(roleService.getRoleById(2));
        user.setUserName("iwona");
        user.setPassword("secure_password");
        user.setMail("iwona@onet.pl");

        boolean isSaved = userService.saveUser(user);

        assertThat(isSaved).isFalse();
    }

    @Test
    void testGetUserById() {
        User user = userService.getUserById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getUserName()).isEqualTo("admin");
    }

    @Test
    void testNotGetUserById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono użytkownika o id: " + 99);
    }

    @Test
    void testFindByEmail() {

        User foundUser = userService.findByEmail("iwona@onet.pl");
        assertThat(foundUser.getId()).isEqualTo(2L);
    }

    @Test
    void testFindByEmail2() {

        User foundUser = userService.findByEmail("klaudia06072002@gmail.com");
        assertThat(foundUser.getId()).isEqualTo(7L);
    }

    @Test
    void testFindByResetToken() {

        User foundUser = userService.findByResetToken("cf6ccbd6-cd7f-4d4d-91ec-e5a9ed048e6b");
        assertThat(foundUser.getId()).isEqualTo(7L);
    }

    @Test
    void testFindByUserName() {

        User foundUser = userService.findByUserName("iwona");
        assertThat(foundUser.getId()).isEqualTo(2L);
    }


    @Test
    void testUpdateUser() {
        User user = userService.getUserById(1L);
        user.setUserName("updated_user_name");

        boolean isUpdated = userService.updateUser(user);

        assertThat(isUpdated).isTrue();

        User updatedUser = userService.getUserById(user.getId());
        assertThat(updatedUser.getUserName()).isEqualTo("updated_user_name");
    }

    @Test
    void testDeleteUser() {
        User userToDelete = new User();
        userToDelete.setId(1L);

        boolean result = userService.deleteUser(userToDelete);

        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono użytkownika o id: " + 1);
    }

    @Test
    void testNotDeleteUser() {
        User nonExistentUser = new User();
        nonExistentUser.setId(99L);

        boolean result = userService.deleteUser(nonExistentUser);

        assertThat(result).isFalse();
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertThat(users).hasSize(5);
    }

    @Test
    void testGetGenealogists() {

        List<User> users = userService.getGenealogists();

        assertThat(users).hasSize(2);

        assertThat(users)
                .extracting(User::getId)
                .containsExactly(1L, 5L);
    }

    @Test
    void testGetUsers() {

        List<User> users = userService.getUsers();

        assertThat(users).hasSize(3);

        assertThat(users)
                .extracting(User::getId)
                .containsExactly(2L, 3L, 4L);
    }

    @Test
    void testCreatePasswordResetToken() {

        User user = userService.getUserById(2L);

        userService.saveUser(user);

        String token = userService.createPasswordResetToken(user);

        assertThat(token).isNotNull();
        assertThat(user.getResetToken()).isEqualTo(token);

        LocalDateTime expectedExpirationTime = LocalDateTime.now().plusMinutes(30);

        assertThat(user.getTokenExpirationTime()).isAfter(LocalDateTime.now())
                .isBefore(expectedExpirationTime.plusSeconds(1));
    }

    @Test
    void testSendResetPasswordEmail() {

        User user = userService.getUserById(7L);
        userService.saveUser(user);

        String token = userService.createPasswordResetToken(user);
        String email = "klaudia06072002@gmail.com";

        userService.sendResetPasswordEmail(email, token);
    }

}
