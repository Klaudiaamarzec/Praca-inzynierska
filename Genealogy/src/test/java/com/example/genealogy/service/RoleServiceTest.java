package com.example.genealogy.service;

import com.example.genealogy.model.Role;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    void testExistsById() {
        boolean exists = roleService.existsById(1);
        assertThat(exists).isTrue();
    }

    @Test
    void testRoleExists() {

        Role role = new Role();
        role.setRoleName("user");

        boolean result = roleService.roleExists(role);
        assertThat(result).isTrue();
    }

    @Test
    void testRoleNotExists() {

        Role role = new Role();
        role.setRoleName("users");

        boolean result = roleService.roleExists(role);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveRole() {
        Role role = new Role();
        role.setRoleName("admin");

        boolean isSaved = roleService.saveRole(role);

        assertThat(isSaved).isTrue();
        assertThat(roleService.getAllRoles()).contains(role);
    }

    @Test
    void testNotSaveRoleWithEmptyName() {
        Role role = new Role();
        role.setRoleName(null);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            roleService.saveRole(role);
        });

        // Sprawdzenie czy wyjątek zawiera odpowiednią wiadomość walidacyjną
        assertThat(thrown.getConstraintViolations()).hasSize(1); // Weryfikacja liczby błędów walidacji

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Nazwa roli nie może być pusta");
    }

    @Test
    void testNotSaveRole2() {
        Role role = new Role();
        role.setRoleName("genealogist");

        boolean isSaved = roleService.saveRole(role);

        assertThat(isSaved).isFalse();
    }

    @Test
    void testGetRoleById() {
        Role role = roleService.getRoleById(1);

        assertThat(role).isNotNull();
        assertThat(role.getRoleName()).isEqualTo("genealogist");
    }

    @Test
    void testNotGetRoleById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            roleService.getRoleById(99);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono roli o id: " + 99);
    }

    @Test
    void testUpdateRole() {
        Role role = roleService.getRoleById(1);
        role.setRoleName("admin");

        boolean isUpdated = roleService.updateRole(role);

        assertThat(isUpdated).isTrue();

        Role updatedRole = roleService.getRoleById(role.getId());
        assertThat(updatedRole.getRoleName()).isEqualTo("admin");
    }

    @Test
    void testDeleteRole() {
        Role roleToDelete = new Role();
        roleToDelete.setId(1);

        boolean result = roleService.deleteRole(roleToDelete);

        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            roleService.getRoleById(1);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono roli o id: " + 1);
    }

    @Test
    void testNotDeleteRole() {
        Role nonExistentRole = new Role();
        nonExistentRole.setId(99);

        boolean result = roleService.deleteRole(nonExistentRole);

        assertThat(result).isFalse();
    }

    @Test
    void testGetAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        assertThat(roles).hasSize(2);
    }

    @Test
    void testFindByRoleName() {

        Role role = roleService.findByRoleName("user");
        assertThat(role.getRoleName()).isEqualTo("user");
    }
}
