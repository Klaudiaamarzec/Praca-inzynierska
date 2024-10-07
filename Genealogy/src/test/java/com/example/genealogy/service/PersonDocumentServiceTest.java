package com.example.genealogy.service;

import com.example.genealogy.model.PersonDocument;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonDocumentServiceTest {

    @Autowired
    private PersonDocumentService personDocumentService;

    @Autowired
    private PersonService personService;

    @Autowired
    private DocumentService documentService;

    @Test
    void testExistsById() {
        boolean result = personDocumentService.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testPersonDocumentExist() {
        PersonDocument personDocument = new PersonDocument();
        personDocument.setDocument(documentService.getDocumentById(7L));
        personDocument.setPerson(personService.getPersonById(8L));

        boolean result = personDocumentService.personDocumentExists(personDocument);
        assertThat(result).isTrue();
    }

    @Test
    void testPersonDocumentNotExist() {
        PersonDocument personDocument = new PersonDocument();
        personDocument.setDocument(documentService.getDocumentById(1L));
        personDocument.setPerson(personService.getPersonById(5L));

        boolean result = personDocumentService.personDocumentExists(personDocument);
        assertThat(result).isFalse();
    }

    @Test
    void testSavePersonDocument() {
        PersonDocument personDocument = new PersonDocument();
        personDocument.setDocument(documentService.getDocumentById(3L));
        personDocument.setPerson(personService.getPersonById(2L));

        boolean isSaved = personDocumentService.savePersonDocument(personDocument);
        assertThat(isSaved).isTrue();
        assertThat(personDocumentService.getAllPersonDocuments()).contains(personDocument);
    }

    @Test
    void testNotSavePersonDocument() {
        PersonDocument personDocument = new PersonDocument();
        personDocument.setDocument(documentService.getDocumentById(2L));
        personDocument.setPerson(personService.getPersonById(8L));

        boolean isSaved = personDocumentService.savePersonDocument(personDocument);
        assertThat(isSaved).isFalse();
    }

    @Test
    void testNotSavePersonDocumentWithValidation() {
        PersonDocument personDocument = new PersonDocument();
        personDocument.setDocument(documentService.getDocumentById(2L));

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> personDocumentService.savePersonDocument(personDocument));

        assertThat(thrown.getMessage()).contains("Osoba nie może być pusta");
    }

    @Test
    void testGetPersonDocumentById() {
        PersonDocument personDocument = personDocumentService.getPersonDocumentById(5L);
        assertThat(personDocument).isNotNull();
        assertThat(personDocument.getPerson().getId()).isEqualTo(1L);
    }

    @Test
    void testNotGetPersonDocumentById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> personDocumentService.getPersonDocumentById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono osoby w dokumencie o id: " + 99L);
    }

    @Test
    void testUpdatePersonDocument() {
        PersonDocument personDocument = personDocumentService.getPersonDocumentById(1L);
        personDocument.setPerson(personService.getPersonById(3L));

        boolean isUpdated = personDocumentService.updatePersonDocument(personDocument);
        assertThat(isUpdated).isTrue();

        PersonDocument updatedPersonDocument = personDocumentService.getPersonDocumentById(personDocument.getId());
        assertThat(updatedPersonDocument.getPerson().getId()).isEqualTo(3L);
    }

    @Test
    void testDeletePersonDocument() {
        PersonDocument personDocumentToDelete = new PersonDocument();
        personDocumentToDelete.setId(1L);

        boolean result = personDocumentService.deletePersonDocument(personDocumentToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> personDocumentService.getPersonDocumentById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono osoby w dokumencie o id: " + personDocumentToDelete.getId());
    }

    @Test
    void testNotDeletePersonDocument() {
        PersonDocument nonExistentPersonDocument = new PersonDocument();
        nonExistentPersonDocument.setId(99L);

        boolean result = personDocumentService.deletePersonDocument(nonExistentPersonDocument);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllPersonDocuments() {
        List<PersonDocument> personDocuments = personDocumentService.getAllPersonDocuments();
        assertThat(personDocuments).hasSize(11);
    }
}

