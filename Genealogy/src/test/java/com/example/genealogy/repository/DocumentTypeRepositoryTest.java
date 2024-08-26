package com.example.genealogy.repository;

import com.example.genealogy.model.DocumentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentTypeRepositoryTest {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Test
    void testGetTemplate() {

        Optional<DocumentType> optionalDocType = documentTypeRepository.findById(2);

        // Sprawdzenie, czy obiekt został znaleziony
        assertThat(optionalDocType).isPresent();

        // Pobranie obiektu DocumentType z Optional
        DocumentType docType = optionalDocType.get();

        // Sprawdzenie wartości pola template
        assertThat(docType.getTemplate()).isEqualTo("{\"fields\": [{\"name\": \"exampleField1\", \"type\": \"text\"}, {\"name\": \"exampleField2\", \"type\": \"text\"}]}");
    }
}
