package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@SpringJUnitConfig
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testCkeckAddedRole() {

        // Pobranie wszystkich roli z repozytorium
        List<Role> allRoles = roleRepository.findAll();

        // Sprawdzenie, czy liczba ról jest zgodna z oczekiwaniami
        assertThat(allRoles).hasSize(2);

        // Sprawdzenie, czy role są prawidłowe jak w bazie danych
        assertThat(allRoles)
                .extracting(Role::getRoleName)
                .containsExactlyInAnyOrder("genealogist", "user");

    }

    @Test
    void testAddExistingRole() {

        // Pobranie wszystkich roli z repozytorium
        List<Role> allRoles = roleRepository.findAll();

        Role testRole = new Role();
        testRole.setRoleName("genealogist");

    }

    @Test
    void testAddRole() {

        Role newRole = new Role();
        newRole.setRoleName("newRole");
    }
}
