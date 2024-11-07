package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface DocumentService {

    boolean existsById(Long id);

    boolean documentExists(Document document);

    Document getDocumentById(Long id);

    boolean saveDocument(Document document);

    boolean updateDocument(Document document);

    boolean deleteDocument(Document document);

    List<Document> getAllDocuments();

    List<Document> findByNameAndSurname(String name, String surname);

    List<Document> findConfirmedDocuments();

    List<Document> findNotConfirmedDocuments();

    List<Document> findDocumentsByTypeIds(String name, String surname, List<Integer> typeIds);

    List<Document> findDocumentsByDateRange(String name, String surname, LocalDate fromDate, LocalDate toDate);

    List<Document> findDocumentsByPlaces(String name, String surname, List<Long> placeIds);

    List<Document> findDocumentsByOwner(User owner);

    List<Document> searchDocuments(String name, String surname, List<Integer> typeIds, LocalDate fromDate, LocalDate toDate, List<Long> placeIds);

    boolean addPathToDocument(@NotNull Document document, MultipartFile photoFile);
    boolean addPhotoToDocument(Document document, Document photo);

    ResponseEntity<String> approveChanges(Long oldDocumentId, Long newDocumentId);
}
