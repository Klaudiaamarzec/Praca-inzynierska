package com.example.genealogy.service;

import com.example.genealogy.model.DocumentType;
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
public class DocumentTypeServiceTest {

    @Autowired
    private DocumentTypeService documentTypeService;

    @Test
    void testExistsById() {
        boolean result = documentTypeService.existsById(1);
        assertThat(result).isTrue();
    }

    @Test
    void testDocumentTypeExist() {

        DocumentType documentType = new DocumentType();
        documentType.setTypeName("school report");
        documentType.setTemplate("{\"fields\": [{\"name\": \"exampleField1\", \"type\": \"text\"}, {\"name\": \"exampleField2\", \"type\": \"text\"}]}");
        boolean result = documentTypeService.documentTypeExists(documentType);
        assertThat(result).isTrue();
    }

    @Test
    void testDocumentTypeNotExist() {

        DocumentType documentType = new DocumentType();
        documentType.setTypeName("school report");
        documentType.setTemplate("{\"fields\": [{\"name\": \"exampleField1\"}, {\"name\": \"exampleField2\", \"type\": \"text\"}]}");
        boolean result = documentTypeService.documentTypeExists(documentType);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveDocumentType() {

        DocumentType documentType = new DocumentType();
        documentType.setTypeName("Nowy typ dokumentu");
        documentType.setTemplate("{\"fields\": [{\"name\": \"exampleField1\"}, {\"name\": \"exampleField2\", \"type\": \"text\"}]}");

        boolean isSaved = documentTypeService.saveDocumentType(documentType);
        assertThat(isSaved).isTrue();
        assertThat(documentTypeService.getAllDocumentTypes()).contains(documentType);
    }

    @Test
    void testNotSaveDocumentType() {
        DocumentType documentType = new DocumentType();
        documentType.setTypeName(null);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> documentTypeService.saveDocumentType(documentType));

        assertThat(thrown.getMessage()).contains("Nazwa typu nie może być pusta");
    }

    @Test
    void testNotSaveDocumentType2() {

        DocumentType documentType = new DocumentType();
        documentType.setTypeName("school report");
        documentType.setTemplate("{\"fields\": [{\"name\": \"exampleField1\", \"type\": \"text\"}, {\"name\": \"exampleField2\", \"type\": \"text\"}]}");

        boolean result = documentTypeService.saveDocumentType(documentType);
        assertThat(result).isFalse();
    }

    @Test
    void testGetDocumentTypeById() {
        DocumentType documentType = documentTypeService.getDocumentTypeById(1);
        assertThat(documentType).isNotNull();
        assertThat(documentType.getTypeName()).isEqualTo("photography"); // Replace with actual expected name
    }

    @Test
    void testNotGetDocumentTypeById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> documentTypeService.getDocumentTypeById(99));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono typu dokumentu o id: " + 99);
    }

    @Test
    void testUpdateDocumentType() {
        DocumentType documentType = documentTypeService.getDocumentTypeById(1);
        documentType.setTypeName("Zaktualizowany Typ Dokumentu");

        boolean isUpdated = documentTypeService.updateDocumentType(documentType);
        assertThat(isUpdated).isTrue();

        DocumentType updatedDocumentType = documentTypeService.getDocumentTypeById(documentType.getId());
        assertThat(updatedDocumentType.getTypeName()).isEqualTo("Zaktualizowany Typ Dokumentu");
    }

    @Test
    void testDeleteDocumentType() {
        DocumentType documentTypeToDelete = new DocumentType();
        documentTypeToDelete.setId(1);

        boolean result = documentTypeService.deleteDocumentType(documentTypeToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> documentTypeService.getDocumentTypeById(1));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono typu dokumentu o id: " + documentTypeToDelete.getId());
    }

    @Test
    void testNotDeleteDocumentType() {

        DocumentType nonExistentDocumentType = new DocumentType();
        nonExistentDocumentType.setId(99);

        boolean result = documentTypeService.deleteDocumentType(nonExistentDocumentType);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllDocumentTypes() {
        List<DocumentType> documentTypes = documentTypeService.getAllDocumentTypes();
        assertThat(documentTypes).hasSize(8);
    }
}
