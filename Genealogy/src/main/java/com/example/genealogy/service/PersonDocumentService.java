package com.example.genealogy.service;

import com.example.genealogy.model.PersonDocument;

import java.util.List;

public interface PersonDocumentService {

    boolean savePersonDocument(PersonDocument personDocument);

    boolean updatePersonDocument(PersonDocument personDocument);

    boolean existsById(PersonDocument personDocument);

    boolean personDocumentExist(PersonDocument personDocument);

    boolean deletePersonDocument(PersonDocument personDocument);

    List<PersonDocument> getAllPersonDocuments();
}
