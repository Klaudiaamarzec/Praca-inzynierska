package com.example.genealogy.service;

import com.example.genealogy.model.Person;
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
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private DocumentService documentService;

    @Test
    void testExistsById() {
        boolean result = personService.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testPersonExists() {
        Person person = new Person();
        person.setName("Kylie");
        person.setSurname("Jenner");

        boolean result = personService.personExists(person);

        assertThat(result).isTrue();
    }

    @Test
    void testPersonNotExists() {
        Person person = new Person();
        person.setName("Kylie");
        person.setSurname("Kardashian");

        boolean result = personService.personExists(person);
        assertThat(result).isFalse();
    }

    @Test
    void testPersonNotExists2() {
        Person person = new Person();
        person.setName(null);
        person.setSurname(null);

        boolean result = personService.personExists(person);
        assertThat(result).isFalse();
    }

    @Test
    void testSavePerson() {
        Person person = new Person();
        person.setName("John");
        person.setSurname("Doe");

        boolean isSaved = personService.savePerson(person);
        assertThat(isSaved).isTrue();
        assertThat(personService.getAllPeople()).contains(person);
    }

    @Test
    void testNotSavePerson() {
        Person person = new Person();
        person.setName("Kylie");
        person.setSurname("Jenner");

        boolean isSaved = personService.savePerson(person);
        assertThat(isSaved).isFalse();
    }

    @Test
    void testNotSavePersonWithValidation() {
        Person person = new Person();
        person.setName(null);
        person.setSurname(null);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> personService.savePerson(person));

        assertThat(thrown.getConstraintViolations()).hasSize(2);

        // Sprawdź wiadomości o błędach
        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Imię nie może być puste");
        assertThat(messages).contains("Nazwisko nie może być puste");
    }


    @Test
    void testGetPersonById() {
        Person person = personService.getPersonById(5L);
        assertThat(person).isNotNull();
        assertThat(person.getName()).isEqualTo("Jacques");
    }

    @Test
    void testNotGetPersonById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> personService.getPersonById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono osoby o id: " + 99L);
    }

    @Test
    void testUpdatePerson() {
        Person person = personService.getPersonById(4L);
        person.setName("Kendy");

        boolean isUpdated = personService.updatePerson(person);
        assertThat(isUpdated).isTrue();

        Person updatedPerson = personService.getPersonById(person.getId());
        assertThat(updatedPerson.getName()).isEqualTo("Kendy");
    }

    @Test
    void testDeletePerson() {
        Person personToDelete = new Person();
        personToDelete.setId(1L);

        boolean result = personService.deletePerson(personToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> personService.getPersonById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono osoby o id: " + personToDelete.getId());
    }

    @Test
    void testNotDeletePerson() {
        Person nonExistentPerson = new Person();
        nonExistentPerson.setId(99L);

        boolean result = personService.deletePerson(nonExistentPerson);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllPersons() {
        List<Person> persons = personService.getAllPeople();
        assertThat(persons).hasSize(8);
    }

    @Test
    void testGetPersonList() {

        List<Object[]> personList = personService.getPersonList();
        assertThat(personList).hasSize(8);
        assertThat(personList.get(0)).containsExactly("William", "Jenner");
        assertThat(personList.get(7)).containsExactly("Kris", "Jenner");
    }

    @Test
    void testFindPersonByParameter() {

        List<Person> personList = personService.findPersonByParameter("Jenner");

        assertThat(personList).hasSize(4);

        assertThat(personList)
                .extracting(Person::getId)
                .containsExactly(2L, 3L, 4L, 1L);
    }

    @Test
    void testFindPersonByParameter2() {

        List<Person> personList = personService.findPersonByParameter("Kylie Jenner");

        assertThat(personList).hasSize(1);
    }

    @Test
    void testFindPersonByParameter3() {

        List<Person> personList = personService.findPersonByParameter("kim kardash");

        assertThat(personList).hasSize(1);
    }

    @Test
    void testFindAllPersonsInDocument() {

        List<Person> personList = personService.findAllPersonsInDocument(documentService.getDocumentById(7L));

        assertThat(personList).hasSize(3);

        assertThat(personList)
                .extracting(Person::getId)
                .containsExactly(4L, 8L, 1L);
    }

}
