package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.DocumentType;
import com.example.genealogy.repository.DocumentTypeRepository;
import com.example.genealogy.service.DocumentTypeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;
    private final Validator validator;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository, Validator validator) {
        this.documentTypeRepository = documentTypeRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Integer id) {
        return documentTypeRepository.existsById(id);
    }

    @Override
    public boolean documentTypeExists(@NotNull DocumentType documentType) {
        return documentTypeRepository.existsDocumentType(documentType.getTypeName(), documentType.getTemplate());
    }

    @Override
    public DocumentType getDocumentTypeById(Integer id) {
        return documentTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono typu dokumentu o id: " + id));
    }

    @Override
    public boolean saveDocumentType(@NotNull DocumentType documentType) {
        if (documentTypeExists(documentType)) {
            return false;
        }

        validateDocumentType(documentType);

        try {
            documentTypeRepository.save(documentType);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateDocumentType(@NotNull DocumentType documentType) {
        if (!existsById(documentType.getId())) {
            return false;
        }

        validateDocumentType(documentType);

        try {
            documentTypeRepository.save(documentType);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteDocumentType(DocumentType documentType) {
        try {
            if (existsById(documentType.getId())) {
                documentTypeRepository.deleteById(documentType.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    private void validateDocumentType(DocumentType documentType) {
        Set<ConstraintViolation<DocumentType>> violations = validator.validate(documentType);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<DocumentType> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja typu dokumentu nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
