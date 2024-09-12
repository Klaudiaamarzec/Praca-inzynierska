package com.example.genealogy.service;

import com.example.genealogy.model.DocumentType;

import java.util.List;

public interface DocumentTypeService {

    boolean saveDocumentType(DocumentType documentType);

    boolean updateDocumentType(DocumentType documentType);

    boolean existsById(int id);

    boolean deleteDocumentType(DocumentType documentType);

    boolean existsById(Integer id);

    List<DocumentType> getAllDocumentTypes();

}
