package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@SpringJUnitConfig
public class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        // Setup initial data if needed
        // Example: Create and save some documents
        Document document1 = new Document();
        document1.setTitle("Document 1");
        document1.setConfirmed(true);
        document1.setStartDate(LocalDate.now());
        document1.setEndDate(LocalDate.now().plusDays(1));

        documentRepository.save(document1);
    }

    @Test
    void testFindConfirmedDocuments() {
        List<Document> confirmedDocuments = documentRepository.findConfirmedDocuments();
        assertThat(confirmedDocuments).isNotEmpty();
        assertThat(confirmedDocuments.get(0).isConfirmed()).isTrue();
    }

    @Test
    void testFindConfirmedDocumentsPerson() {
        // Setup person if needed
        // Example: Create and save a person

        Person person = new Person();
        person.setName("John");
        person.setSurname("Doe");
        // Save the person and link with document

        List<Document> confirmedDocuments = documentRepository.findConfirmedDocumentsPerson("John", "Doe");
        assertThat(confirmedDocuments).isNotEmpty();
    }

    // Add more tests for other methods
}
