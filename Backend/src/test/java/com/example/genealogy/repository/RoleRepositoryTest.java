package com.example.genealogy.repository;

import com.example.genealogy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void checkIfExist() {

        boolean result = roleRepository.existsRole("genealogist");
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testCheckAddedRoles() {

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
    void testAddRole() {

        // Liczba ról przed dodaniem nowej roli
        long countBeforeAdd = roleRepository.count();

        Role newRole = new Role();
        newRole.setRoleName("newRole");

        // Zapisanie nowej roli do bazy danych
        roleRepository.save(newRole);

        // Liczba ról po dodaniu nowej roli
        long countAfterAdd = roleRepository.count();

        // Pobranie nowo dodanej roli z bazy danych
        Role savedRole = roleRepository.findByRoleName("newRole");

        // Sprawdzenie, czy nowa rola została poprawnie dodana
        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getRoleName()).isEqualTo("newRole");

        // Sprawdzenie, czy liczba ról w bazie danych wzrosła o 1
        assertThat(countAfterAdd).isEqualTo(countBeforeAdd + 1);
    }

    @Test
    void testAddExistingRole() {

        long countBeforeAdd = roleRepository.count();

        // Stworzenie nowej roli o tej samej nazwie
        Role testRole = new Role();
        testRole.setRoleName("genealogist");

        // Uchwyt wyjątku i sprawdzenie jego szczegółów
        DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            roleRepository.save(testRole);
        });

        // Opcjonalnie, sprawdzenie komunikatu błędu
        Assertions.assertTrue(exception.getMessage().contains("duplicate key value violates unique constraint"));
    }
}
