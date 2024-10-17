package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.PersonDocument;
import com.example.genealogy.repository.PersonDocumentRepository;
import com.example.genealogy.service.PersonDocumentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PersonDocumentServiceImpl implements PersonDocumentService {

    private final PersonDocumentRepository personDocumentRepository;
    private final Validator validator;

    @Autowired
    public PersonDocumentServiceImpl(PersonDocumentRepository personDocumentRepository, Validator validator) {
        this.personDocumentRepository = personDocumentRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Long id) {
        return personDocumentRepository.existsById(id);
    }

    @Override
    public boolean personDocumentExists(@NotNull PersonDocument personDocument) {
        return personDocumentRepository.existsPersonDocument(
                personDocument.getPerson() != null ? personDocument.getPerson().getId() : null,
                personDocument.getDocument() != null ? personDocument.getDocument().getId() : null,
                personDocument.getComment(),
                personDocument.getX(),
                personDocument.getY());
    }

    @Override
    public PersonDocument getPersonDocumentById(Long id) {
        return personDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono osoby w dokumencie o id: " + id));
    }

    @Override
    public List<PersonDocument> findPersonDocumentByDocumentID(Long documentID) {
        return personDocumentRepository.findPersonDocumentByDocumentID(documentID);
    }

    @Override
    public PersonDocument getPersonDocument(Long documentID, Long personID) {
        return personDocumentRepository.getPersonDocument(documentID, personID);
    }

    @Override
    public boolean savePersonDocument(@NotNull PersonDocument personDocument) {
        if (personDocumentExists(personDocument)) {
            return false;
        }

        validatePersonDocument(personDocument);

        try {
            personDocumentRepository.save(personDocument);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updatePersonDocument(@NotNull PersonDocument personDocument) {
        if (!existsById(personDocument.getId())) {
            return false;
        }

        validatePersonDocument(personDocument);

        try {
            personDocumentRepository.save(personDocument);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deletePersonDocument(PersonDocument personDocument) {
        try {
            if (existsById(personDocument.getId())) {
                personDocumentRepository.delete(personDocument);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteByDocumentID(Long documentID) {

        try {
            personDocumentRepository.deleteByDocumentId(documentID);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public List<PersonDocument> getAllPersonDocuments() {
        return personDocumentRepository.findAll();
    }

    private void validatePersonDocument(PersonDocument personDocument) {
        Set<ConstraintViolation<PersonDocument>> violations = validator.validate(personDocument);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<PersonDocument> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja dokumentu osoby nie powiodła się:\n" + sb, violations);
        }
    }
}
