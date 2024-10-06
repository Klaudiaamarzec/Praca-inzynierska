package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.DocumentType;

import java.util.List;

public interface DocumentTypeService {

    boolean existsById(Integer id);

    boolean documentTypeExists(DocumentType documentType);

    DocumentType getDocumentTypeById(Integer id);

    boolean saveDocumentType(DocumentType documentType);

    boolean updateDocumentType(DocumentType documentType);

    boolean deleteDocumentType(DocumentType documentType);

    List<DocumentType> getAllDocumentTypes();

}
