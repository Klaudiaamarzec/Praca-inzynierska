package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.model.User;

import java.time.LocalDate;
import java.util.List;

public interface DocumentService {

    boolean saveDocument(Document document);

    boolean updateDocument(Document document);

    boolean existsById(long id);

    boolean deleteDocument(Document document);

    List<Document> getAllDocuments();

    List<Document> findConfirmedDocuments(String name, String surname);

    List<Document> findNotConfirmedDocuments();

    List<Document> findDocumentsForPerson(Person person);

    List<Document> findPhotographs(String name, String surname);

    List<Document> findDocumentsByTypeIds(String name, String surname, List<Integer> typeIds);

    List<Document> findDocumentsByDateRange(String name, String surname, LocalDate fromDate, LocalDate toDate);

    List<Document> findDocumentsByPlaces(String name, String surname, List<Long> placeIds);

    List<Document> findDocumentsByOwner(User owner);

    List<Document> findDocumentsByLocalizations(String name, String surname, List<Long> localizationIDs);

    boolean addPhotoToDocument(Document document, Document photo);
}
