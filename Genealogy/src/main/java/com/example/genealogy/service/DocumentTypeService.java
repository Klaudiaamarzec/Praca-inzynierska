package com.example.genealogy.service;

import com.example.genealogy.model.DocumentType;

import java.util.List;

public interface DocumentTypeService {

    boolean saveDocumentType(DocumentType documentType);

    boolean updateDocumentType(DocumentType documentType);

    boolean existsById(DocumentType documentType);

    boolean documentTypeExist(DocumentType documentType);

    boolean deleteDocumentType(DocumentType documentType);

    List<DocumentType> getAllDocumentTypes();

}
