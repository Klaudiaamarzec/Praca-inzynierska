package com.example.genealogy.service;

import com.example.genealogy.model.Family;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
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
public class FamilyServiceTest {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private PersonService personService;

    @Test
    void testExistsById() {
        boolean result = familyService.existsById(4L);
        assertThat(result).isTrue();
    }

    @Test
    void testFamilyExist() {
        Family family = new Family();
        family.setChild(personService.getPersonById(4L));
        family.setFather(personService.getPersonById(2L));
        family.setMother(personService.getPersonById(1L));
        boolean result = familyService.familyExists(family);
        assertThat(result).isTrue();
    }

    @Test
    void testFamilyNotExist() {
        Family family = new Family();
        family.setChild(personService.getPersonById(2L));
        family.setFather(personService.getPersonById(4L));
        family.setMother(personService.getPersonById(3L));
        boolean result = familyService.familyExists(family);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveFamily() {

        Family family = new Family();
        family.setChild(personService.getPersonById(2L));
        family.setMother(personService.getPersonById(4L));
        family.setFather(personService.getPersonById(3L));

        boolean isSaved = familyService.saveFamily(family);
        assertThat(isSaved).isTrue();
        assertThat(familyService.getAllFamilies()).contains(family);
    }

    @Test
    void testNotSaveFamily() {
        Family family = new Family();
        family.setChild(personService.getPersonById(2L));

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> familyService.saveFamily(family));

        assertThat(thrown.getConstraintViolations()).hasSize(2);

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Pole 'Matka' nie może być puste");
        assertThat(messages).contains("Pole 'Ojciec' nie może być puste");
    }

    @Test
    void testNotSaveFamilyAlreadyExists() {

        Family family = new Family();
        family.setChild(personService.getPersonById(4L));
        family.setFather(personService.getPersonById(2L));
        family.setMother(personService.getPersonById(1L));
        boolean result = familyService.saveFamily(family);
        assertThat(result).isFalse();
    }

    @Test
    void testGetFamilyById() {
        Family family = familyService.getFamilyById(1L);
        assertThat(family).isNotNull();
        assertThat(family.getChild().getId()).isEqualTo(3L);
        assertThat(family.getMother().getId()).isEqualTo(1L);

    }

    @Test
    void testNotGetFamilyById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> familyService.getFamilyById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono rodziny o id: " + 99);
    }

    @Test
    void testUpdateFamily() {
        Family family = familyService.getFamilyById(3L);
        family.setFather(personService.getPersonById(4L));

        boolean isUpdated = familyService.updateFamily(family);
        assertThat(isUpdated).isTrue();

        Family updatedFamily = familyService.getFamilyById(family.getId());
        assertThat(updatedFamily.getFather().getId()).isEqualTo(4L);
    }

    @Test
    void testDeleteFamily() {
        Family familyToDelete = new Family();
        familyToDelete.setId(1L);

        boolean result = familyService.deleteFamily(familyToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> familyService.getFamilyById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono rodziny o id: " + familyToDelete.getId());
    }

    @Test
    void testNotDeleteFamily() {
        Family nonExistentFamily = new Family();
        nonExistentFamily.setId(99L);

        boolean result = familyService.deleteFamily(nonExistentFamily);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllFamilies() {
        List<Family> families = familyService.getAllFamilies();
        assertThat(families).hasSize(4);
    }

    @Test
    void testFindFamiliesByChild() {

        List<Family> families = familyService.findFamiliesByChild(personService.getPersonById(4L));

        assertThat(families).hasSize(1);

        assertThat(families.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testFindFamiliesByMother() {

        List<Family> families = familyService.findFamiliesByMother(personService.getPersonById(1L));

        assertThat(families).hasSize(3);

        assertThat(families.get(0).getId()).isEqualTo(1L);
        assertThat(families.get(1).getId()).isEqualTo(2L);
        assertThat(families.get(2).getId()).isEqualTo(4L);
    }

    @Test
    void testFindFamiliesByFather() {

        List<Family> families = familyService.findFamiliesByFather(personService.getPersonById(2L));

        assertThat(families).hasSize(2);

        assertThat(families.get(0).getId()).isEqualTo(1L);
        assertThat(families.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindFamiliesByParent() {

        List<Family> families = familyService.findFamiliesByParent(personService.getPersonById(2L));

        assertThat(families).hasSize(2);

        assertThat(families.get(0).getId()).isEqualTo(1L);
        assertThat(families.get(1).getId()).isEqualTo(2L);
    }
}
