package com.example.genealogy.service;

import com.example.genealogy.model.PersonDocument;

import java.util.List;

public interface PersonDocumentService {

    boolean existsById(Long id);

    boolean personDocumentExists(PersonDocument personDocument);

    PersonDocument getPersonDocumentById(Long id);

    boolean savePersonDocument(PersonDocument personDocument);

    boolean updatePersonDocument(PersonDocument personDocument);

    boolean deletePersonDocument(PersonDocument personDocument);

    List<PersonDocument> getAllPersonDocuments();
}
