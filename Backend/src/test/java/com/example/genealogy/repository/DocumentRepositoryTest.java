package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void testExist() {
        boolean result = documentRepository.documentExists("List od Kim Kardashian", null, null, null, 4L,6L,3L,8);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testNotExist() {
        boolean result = documentRepository.documentExists("Kardashian", null, null, null, 4L,null,3L,8);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testFindByNameAndSurname() {

        List<Document> confirmedDocuments = documentRepository.findByNameAndSurname("Kim", "kardashian");
        assertThat(confirmedDocuments).hasSize(4);

        // IDs check
        assertThat(confirmedDocuments.get(0).getId()).isEqualTo(1L);
        assertThat(confirmedDocuments.get(1).getId()).isEqualTo(2L);
        assertThat(confirmedDocuments.get(2).getId()).isEqualTo(3L);
        assertThat(confirmedDocuments.get(3).getId()).isEqualTo(7L);
    }

    @Test
    void testFindByTitleAndDescription() {
        List<Document> documents = documentRepository.findDocumentsByTitleAndDescription("List", null);
        assertThat(documents).hasSize(3);
    }

    @Test
    void testFindByTitleAndDescription2() {
        List<Document> documents = documentRepository.findDocumentsByTitleAndDescription(null, "string");
        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(34L);
        assertThat(documents.get(1).getId()).isEqualTo(33L);
    }

    @Test
    void testFindConfirmedDocuments() {

        List<Document> confirmedDocuments = documentRepository.findConfirmedDocuments();
        assertThat(confirmedDocuments).hasSize(4);

        assertThat(confirmedDocuments.get(0).getConfirmed()).isTrue();
        assertThat(confirmedDocuments.get(1).getConfirmed()).isTrue();
        assertThat(confirmedDocuments.get(2).getConfirmed()).isTrue();
        assertThat(confirmedDocuments.get(3).getConfirmed()).isTrue();

        // IDs check
        assertThat(confirmedDocuments.get(0).getId()).isEqualTo(1L);
        assertThat(confirmedDocuments.get(1).getId()).isEqualTo(2L);
        assertThat(confirmedDocuments.get(2).getId()).isEqualTo(6L);
        assertThat(confirmedDocuments.get(3).getId()).isEqualTo(7L);
    }

    @Test
    void testFindConfirmedDocumentsPerson() {

        List<Document> confirmedDocuments = documentRepository.findConfirmedDocumentsPerson("Kris", "Jenner");
        assertThat(confirmedDocuments).hasSize(3);

        assertThat(confirmedDocuments.get(0).getId()).isEqualTo(1);
        assertThat(confirmedDocuments.get(1).getId()).isEqualTo(2);
        assertThat(confirmedDocuments.get(2).getId()).isEqualTo(7);

    }

    @Test
    void testFindNotConfirmedDocuments() {

        List<Document> notConfirmedDocuments = documentRepository.findNotConfirmedDocuments();
        assertThat(notConfirmedDocuments).hasSize(3);

        assertThat(notConfirmedDocuments.get(0).getId()).isEqualTo(3);
        assertThat(notConfirmedDocuments.get(1).getId()).isEqualTo(4);
        assertThat(notConfirmedDocuments.get(2).getId()).isEqualTo(5);
    }

    @Test
    void testFindDocumentsForPerson() {

        List<Document> documents = documentRepository.findDocumentsForPerson(8L);
        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(2);
        assertThat(documents.get(2).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsByType() {

        List<Document> documents = documentRepository.findDocumentsByType(1);
        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(5);
    }

    @Test
    void testFindDocumentsPersonByType() {

        List<Document> documents = documentRepository.findDocumentsPersonByType("Kris", "Jenner", 1);
        assertThat(documents).hasSize(1);

        assertThat(documents.get(0).getId()).isEqualTo(1);
    }

    @Test
    void testFindPhotographs() {

        List<Document> documents = documentRepository.findPhotographs();
        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(5);
    }

    @Test
    void testFindPersonPhotographs() {

        List<Document> documents = documentRepository.findPersonPhotographs("Kendall", "Jenner");
        assertThat(documents).hasSize(1);

        assertThat(documents.get(0).getId()).isEqualTo(1);
    }

    @Test
    void testNotFindPersonPhotographs() {

        List<Document> documents = documentRepository.findPersonPhotographs("Kylie", "Jenner");
        assertThat(documents).hasSize(0);
    }

    @Test
    void testFindDocumentsByTypeIds() {

        List<Integer> list = List.of(3,5,7);
        List<Document> documents = documentRepository.findDocumentsByTypeIds(list);
        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(4);
        assertThat(documents.get(1).getId()).isEqualTo(6);
        assertThat(documents.get(2).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsPersonByTypeIds() {

        List<Integer> list = List.of(1,2,7);
        List<Document> documents = documentRepository.findDocumentsPersonByTypeIds("Kris", "Jenner", list);
        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(2);
        assertThat(documents.get(2).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsPersonByTypeIds2() {

        List<Integer> list = List.of(1,8,7);
        List<Document> documents = documentRepository.findDocumentsPersonByTypeIds("Kim", "Kardashian", list);
        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(3);
        assertThat(documents.get(2).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsByDateRangeStartEnd() {

        LocalDate startDate = LocalDate.of(2010, 2, 6);
        LocalDate endDate = LocalDate.of(2021, 11, 2);

        List<Document> documents = documentRepository.findDocumentsByDateRangeStartEnd(startDate, endDate);

        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(2);
        assertThat(documents.get(1).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsByDateRangeStartEnd2() {

        LocalDate startDate = LocalDate.of(2009, 2, 1);
        LocalDate endDate = LocalDate.of(2010, 1, 2);

        List<Document> documents = documentRepository.findDocumentsByDateRangeStartEnd(startDate, endDate);

        assertThat(documents).hasSize(1);

        assertThat(documents.get(0).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsPersonByDateRangeStartEnd()  {

        LocalDate startDate = LocalDate.of(2010, 2, 6);
        LocalDate endDate = LocalDate.of(2021, 11, 2);

        List<Document> documents = documentRepository.findDocumentsPersonByDateRangeStartEnd("Kim", "kardashian", startDate, endDate);
        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(2);
        assertThat(documents.get(1).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsByDate() {

        List<Long> list = List.of(3L,6L,7L,9L);

        List<Document> documents = documentRepository.findDocumentsByDates(list);

        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(6);
    }

    @Test
    void testFindDocumentsByDate2() {

        List<Long> list = List.of(3L,4L,2L,9L);

        List<Document> documents = documentRepository.findDocumentsByDates(list);

        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(3);
        assertThat(documents.get(1).getId()).isEqualTo(4);
        assertThat(documents.get(2).getId()).isEqualTo(5);
    }

    @Test
    void testFindDocumentsPersonByDate() {

        List<Long> list = List.of(3L,6L,7L,9L);
        List<Document> documents = documentRepository.findDocumentsPersonByDates("kendall", "jenner", list);

        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(6);
    }

    @Test
    void testFindDocumentsByPlaces() {

        List<Long> places = List.of(1L,2L,3L,4L);
        List<Document> documents = documentRepository.findDocumentsByPlaces(places);
        assertThat(documents).hasSize(4);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(4);
        assertThat(documents.get(2).getId()).isEqualTo(5);
        assertThat(documents.get(3).getId()).isEqualTo(6);
    }

    @Test
    void testFindDocumentsPersonByPlaces() {

        List<Long> places = List.of(1L,2L,7L,8L);
        List<Document> documents = documentRepository.findDocumentsPersonByPlaces("kris", "jenner", places);
        assertThat(documents).hasSize(3);

        assertThat(documents.get(0).getId()).isEqualTo(1);
        assertThat(documents.get(1).getId()).isEqualTo(2);
        assertThat(documents.get(2).getId()).isEqualTo(7);
    }

    @Test
    void testFindDocumentsByOwner() {

        List<Document> documents = documentRepository.findDocumentsByOwner(2);
        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(4);
        assertThat(documents.get(1).getId()).isEqualTo(5);
    }

    @Test
    void testFindDocumentsByLocalizations() {

        List<Long> localizations = List.of(1L,2L);
        List<Document> documents = documentRepository.findDocumentsByLocalizations(localizations);
        assertThat(documents).hasSize(1);

        assertThat(documents.get(0).getId()).isEqualTo(2);
    }

    @Test
    void testFindDocumentsPersonByLocalization() {

        List<Long> localizations = List.of(1L,2L, 3L);
        List<Document> documents = documentRepository.findDocumentsPersonByLocalization("kim", "kardashian", localizations);
        assertThat(documents).hasSize(2);

        assertThat(documents.get(0).getId()).isEqualTo(2);
        assertThat(documents.get(1).getId()).isEqualTo(7);
    }

    @Test
    @Commit // Commit changes to database
    void testCorrectAddPhotoToDocument() {

        Document photoRefers = documentRepository.findById(4L).orElseThrow();
        // Photo = 5
        documentRepository.addPhotoToDocument(5, photoRefers);

        Document updatedPhoto = documentRepository.findById(5L).orElseThrow();
        assertThat(updatedPhoto.getPhotoRefers().getId()).isEqualTo(photoRefers.getId());
    }

    @Test
    @Commit // Commit changes to database
    void testNotCorrectAddPhotoToDocument() {

        Document photoRefers = documentRepository.findById(4L).orElseThrow();
        // Photo = 3
        documentRepository.addPhotoToDocument(3, photoRefers);

        Document updatedPhoto = documentRepository.findById(3L).orElseThrow();
        assertThat(updatedPhoto.getPhotoRefers()).isNull();
    }
}
