package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DateService dateService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Test
    void testExistsById() {
        boolean result = documentService.existsById(7L);
        assertThat(result).isTrue();
    }

    @Test
    void testDocumentExist() {

        Document document = new Document();
        document.setConfirmed(false);
        document.setTitle("Akt urodzenia Stormi Webster");
        document.setDate(dateService.getDateById(2L));
        document.setPlace(addressService.getAddressById(1L));
        document.setOwner(userService.getUserById(2L));
        document.setType(documentTypeService.getDocumentTypeById(3));

        boolean result = documentService.documentExists(document);
        assertThat(result).isTrue();
    }

    @Test
    void testDocumentNotExist() {

        Document document = new Document();
        document.setConfirmed(true);
        document.setTitle("Akt urodzenia Stormi Webster");

        boolean result = documentService.documentExists(document);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveDocument() {

        Document document = new Document();
        document.setConfirmed(true);
        document.setTitle("Akt urodzenia Kylie Jenner");
        document.setDate(dateService.getDateById(2L));
        document.setPlace(addressService.getAddressById(3L));
        document.setOwner(userService.getUserById(2L));
        document.setType(documentTypeService.getDocumentTypeById(3));

        boolean isSaved = documentService.saveDocument(document);
        assertThat(isSaved).isTrue();
        assertThat(documentService.getAllDocuments()).contains(document);
    }

    @Test
    void testNotSaveDocument() {

        Document document = new Document();
        document.setConfirmed(false);
        document.setTitle("Akt urodzenia Stormi Webster");
        document.setDate(dateService.getDateById(2L));
        document.setPlace(addressService.getAddressById(1L));
        document.setOwner(userService.getUserById(2L));
        document.setType(documentTypeService.getDocumentTypeById(3));

        boolean isSaved = documentService.saveDocument(document);
        assertThat(isSaved).isFalse();
    }

    @Test
    void testNotSaveDocument2() {

        Document document = new Document();
        document.setConfirmed(true);
        document.setTitle("Akt urodzenia Kylie Jenner");
        document.setDate(dateService.getDateById(2L));
        document.setOwner(userService.getUserById(2L));
        document.setType(documentTypeService.getDocumentTypeById(3));

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            documentService.saveDocument(document);
        });

        assertThat(thrown.getMessage()).contains("Adres nie może być pusty");
    }

    @Test
    void testGetDocumentById() {
        Document document = documentService.getDocumentById(5L);
        assertThat(document).isNotNull();
        assertThat(document.getTitle()).isEqualTo("Zdjęcie aktu urodzenia Stormi Webster");
    }

    @Test
    void testNotGetDocumentById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            documentService.getDocumentById(99L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono dokumentu o id: " + 99L);
    }

    @Test
    void testUpdateDocument() {

        Document document = new Document();
        document.setConfirmed(true);
        document.setTitle("Akt małżeństwa Kylie Jenner");
        document.setDate(dateService.getDateById(6L));
        document.setPlace(addressService.getAddressById(3L));
        document.setOwner(userService.getUserById(4L));
        document.setType(documentTypeService.getDocumentTypeById(2));

        documentService.saveDocument(document);

        document.setTitle("Akt małżeństwa Kylie Jenner i Jacquesa Webstera");

        boolean isUpdated = documentService.updateDocument(document);
        assertThat(isUpdated).isTrue();

        Document updatedDocument = documentService.getDocumentById(document.getId());
        assertThat(updatedDocument.getTitle()).isEqualTo("Akt małżeństwa Kylie Jenner i Jacquesa Webstera");
    }

    @Test
    void testDeleteDocument() {

        Document documentToDelete = new Document();
        documentToDelete.setId(1L);

        boolean result = documentService.deleteDocument(documentToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            documentService.getDocumentById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono dokumentu o id: " + documentToDelete.getId());
    }

    @Test
    void testNotDeleteDocument() {
        Document nonExistentDocument = new Document();
        nonExistentDocument.setId(99L);

        boolean result = documentService.deleteDocument(nonExistentDocument);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        assertThat(documents).hasSize(7);
    }

    @Test
    void testFindByNameAndSurname() {

        List<Document> documents = documentService.findByNameAndSurname("kim", "Kardashian");

        assertThat(documents).hasSize(4);

        assertThat(documents.get(0).getId()).isEqualTo(1L);
        assertThat(documents.get(1).getId()).isEqualTo(2L);
        assertThat(documents.get(2).getId()).isEqualTo(3L);
        assertThat(documents.get(3).getId()).isEqualTo(7L);
    }

    @Test
    void testFindConfirmedDocuments() {

        List<Document> documents = documentService.findConfirmedDocuments();

        assertThat(documents).hasSize(4);

        assertThat(documents.get(0).getId()).isEqualTo(1L);
        assertThat(documents.get(1).getId()).isEqualTo(2L);
        assertThat(documents.get(2).getId()).isEqualTo(6L);
        assertThat(documents.get(3).getId()).isEqualTo(7L);
    }

    @Test
    void testFindNotConfirmedDocuments() {

        List<Document> documents = documentService.findNotConfirmedDocuments();

        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(3L);
        assertThat(documents.get(1).getId()).isEqualTo(4L);
        assertThat(documents.get(2).getId()).isEqualTo(5L);
    }

    @Test
    void testFindDocumentsByTypeIds() {

        List<Document> documents = documentService.findDocumentsByTypeIds(null,null,List.of(1, 2, 3));

        assertThat(documents).hasSize(4);

        assertThat(documents.get(0).getId()).isEqualTo(1L);
        assertThat(documents.get(1).getId()).isEqualTo(2L);
        assertThat(documents.get(2).getId()).isEqualTo(4L);
        assertThat(documents.get(3).getId()).isEqualTo(5L);
    }

    @Test
    void testFindDocumentsByTypeIds2() {

        List<Document> documents = documentService.findDocumentsByTypeIds("Kim","Kardashian",List.of(1, 2, 3));

        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(1L);
        assertThat(documents.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindDocumentsByTypeIds3() {

        List<Document> documents = documentService.findDocumentsByTypeIds("Kim",null,List.of(1, 2, 3));

        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(1L);
        assertThat(documents.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindDocumentsByDateRange() {

        List<Document> documents = documentService.findDocumentsByDateRange(null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2015,12,31));

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(3L, 6L, 7L, 2L);

    }

    @Test
    void testFindDocumentsByDateRange2() {

        List<Document> documents = documentService.findDocumentsByDateRange(null, null, LocalDate.of(2010, 1, 1), LocalDate.of(2017,12,31));

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(4L, 5L, 2L, 7L);

    }

    @Test
    void testFindDocumentsByDateRange3() {

        List<Document> documents = documentService.findDocumentsByDateRange(null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2010,12,31));

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(3L,6L,7L,2L);

    }

    @Test
    void testFindDocumentsByDateRange4() {

        List<Document> documents = documentService.findDocumentsByDateRange("Kim", "Kardashian", LocalDate.of(2000, 1, 1), LocalDate.of(2010,12,31));

        assertThat(documents).hasSize(3);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(3L,7L, 2L);

    }

    @Test
    void testFindDocumentsByDateRange5() {

        List<Document> documents = documentService.findDocumentsByDateRange(null, "Jenner", LocalDate.of(1999, 1, 1), LocalDate.of(2012,12,31));

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 6L, 7L, 2L);

    }

    @Test
    void testFindDocumentsByPlaces() {

        List<Document> documents = documentService.findDocumentsByPlaces(null, null, List.of(1L, 6L));

        assertThat(documents).hasSize(3);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(3L,4L,5L);
    }

    @Test
    void testFindDocumentsByPlaces2() {

        List<Document> documents = documentService.findDocumentsByPlaces("Kim", "Kardashian", List.of(2L, 7L, 6L, 8L));

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 2L, 3L,7L);
    }

    @Test
    void testFindDocumentsByPlaces3() {

        List<Document> documents = documentService.findDocumentsByPlaces("Kim", null, List.of(2L, 7L, 6L, 8L));

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 2L, 3L, 7L);
    }

    @Test
    void testFindDocumentsByOwner() {

        List<Document> documents = documentService.findDocumentsByOwner(userService.getUserById(2L));

        assertThat(documents).hasSize(2);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(4L, 5L);
    }

    @Test
    void testSearchDocument() {

        List<Document> documents = documentService.searchDocuments(null, null, null, null, null, null);

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 2L, 6L, 7L);
    }

    @Test
    void testSearchDocument2() {

        List<Document> documents = documentService.searchDocuments("Kim", "Kardashian", null, null, null, null);

        assertThat(documents).hasSize(3);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 2L, 7L);
    }

    @Test
    void testSearchDocument3() {

        List<Document> documents = documentService.searchDocuments("Kim", null, null, null, null, null);

        assertThat(documents).hasSize(3);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 2L, 7L);
    }

    @Test
    void testSearchDocument4() {

        List<Document> documents = documentService.searchDocuments(null, "jenner", null, null, null, null);

        assertThat(documents).hasSize(4);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 2L, 7L, 6L);
    }

    @Test
    void testSearchDocument5() {

        List<Document> documents = documentService.searchDocuments(null, "jenner", null, null, null, List.of(2L, 8L));

        assertThat(documents).hasSize(3);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 7L, 6L);
    }

    @Test
    void testSearchDocument6() {

        List<Document> documents = documentService.searchDocuments(null, "jenner", null, LocalDate.of(1999,1,1), LocalDate.of(2003,12,31), List.of(2L, 8L));

        assertThat(documents).hasSize(2);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 6L);
    }

    @Test
    void testSearchDocument7() {

        List<Document> documents = documentService.searchDocuments(null, "jenner", null, LocalDate.of(2003,1,1), LocalDate.of(2010,12,31), List.of(2L, 8L));

        assertThat(documents).hasSize(2);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(7L, 6L);
    }

    @Test
    void testSearchDocument8() {

        List<Document> documents = documentService.searchDocuments(null, "jenner", List.of(5), LocalDate.of(1999,1,1), LocalDate.of(2003,12,31), List.of(2L, 8L));

        assertThat(documents).hasSize(1);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(6L);
    }

    @Test
    void testSearchDocument9() {

        List<Document> documents = documentService.searchDocuments(null, "jenner", List.of(1, 5), LocalDate.of(1999,1,1), LocalDate.of(2003,12,31), List.of(2L, 8L));

        assertThat(documents).hasSize(2);

        assertThat(documents)
                .extracting(Document::getId)
                .containsExactly(1L, 6L);
    }

    @Test
    void testAddPhotoToDocument() {

        Document document = documentService.getDocumentById(2L);
        Document photo = documentService.getDocumentById(1L);

        boolean result = documentService.addPhotoToDocument(document, photo);
        assertThat(result).isTrue();

        assertThat(photo.getPhotoRefers()).isEqualTo(document);
    }

    @Test
    void testAddPhotoToDocument_ThrowsException_WhenPhotoIsNotOfTypeOne() {

        Document document = documentService.getDocumentById(2L);
        Document nonPhoto = documentService.getDocumentById(6L);

        // Sprawdzenie, czy wyjątek jest rzucany
        assertThrows(IllegalArgumentException.class, () -> {
            documentService.addPhotoToDocument(document, nonPhoto);
        });
    }

}
