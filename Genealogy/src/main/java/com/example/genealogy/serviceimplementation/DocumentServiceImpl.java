package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Date;
import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.model.User;
import com.example.genealogy.repository.DocumentRepository;
import com.example.genealogy.service.DateService;
import com.example.genealogy.service.DocumentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DateService dateService;
    private final Validator validator;

    public DocumentServiceImpl(DocumentRepository documentRepository, DateService dateService, Validator validator) {
        this.documentRepository = documentRepository;
        this.dateService = dateService;
        this.validator = validator;
    }
    @Override
    public boolean saveDocument(@NotNull Document document) {
        if (existsById(document.getId())) {
            return false;
        }

        validateDocument(document);

        try {
            documentRepository.save(document);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateDocument(@NotNull Document document) {
        if (!existsById(document.getId())) {
            return false;
        }

        validateDocument(document);

        try {
            documentRepository.save(document);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(long id) {
        return documentRepository.existsById(id);
    }

    @Override
    public boolean deleteDocument(Document document) {
        try {
            if (existsById(document.getId())) {
                documentRepository.deleteById(document.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public List<Document> findConfirmedDocuments(String name, String surname) {
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            return documentRepository.findConfirmedDocumentsPerson(name, surname);
        } else {
            return documentRepository.findConfirmedDocuments();
        }
    }

    @Override
    public List<Document> findNotConfirmedDocuments() {
        return documentRepository.findNotConfirmedDocuments();
    }

    @Override
    public List<Document> findDocumentsForPerson(@NotNull Person person) {
        return documentRepository.findDocumentsForPerson(person.getId());
    }

    @Override
    public List<Document> findPhotographs(String name, String surname) {
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            return documentRepository.findPersonPhotographs(name, surname);
        } else {
            return documentRepository.findPhotographs();
        }
    }

    @Override
    public List<Document> findDocumentsByTypeIds(String name, String surname, List<Integer> typeIds) {
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            return documentRepository.findDocumentsPersonByTypeIds(name, surname, typeIds);
        } else {
            return documentRepository.findDocumentsByTypeIds(typeIds);
        }
    }

    @Override
    public List<Document> findDocumentsByDateRange(String name, String surname, LocalDate fromDate, LocalDate toDate) {
        // Pobierz daty mieszczące się w przedziale
        List<Date> datesInRange = dateService.findDatesByDateRange(fromDate, toDate);
        List<Long> dateIds = datesInRange.stream().map(Date::getId).collect(Collectors.toList());

        // Inicjalizuj listę dokumentów
        List<Document> documents = new ArrayList<>();

        // Wyszukaj dokumenty na podstawie identyfikatorów dat
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            documents.addAll(documentRepository.findDocumentsPersonByDates(name, surname, dateIds));
        } else {
            documents.addAll(documentRepository.findDocumentsByDates(dateIds));
        }

        // Dodaj dokumenty znalezione w przedziale dat
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            documents.addAll(documentRepository.findDocumentsPersonByDateRangeStartEnd(name, surname, fromDate, toDate));
        } else {
            documents.addAll(documentRepository.findDocumentsByDateRangeStartEnd(fromDate, toDate));
        }

        // Usuwanie duplikatów na podstawie identyfikatora dokumentu
        return documents.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Document> findDocumentsByPlaces(String name, String surname, List<Long> placeIds) {
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            return documentRepository.findDocumentsPersonByPlaces(name, surname, placeIds);
        } else {
            return documentRepository.findDocumentsByPlaces(placeIds);
        }
    }

    @Override
    public List<Document> findDocumentsByOwner(@NotNull User owner) {
        return documentRepository.findDocumentsByOwner(owner.getId());
    }

    @Override
    public List<Document> findDocumentsByLocalizations(String name, String surname, List<Long> localizationIDs) {
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
            return documentRepository.findDocumentsPersonByLocalization(name, surname, localizationIDs);
        } else {
            return documentRepository.findDocumentsByLocalizations(localizationIDs);
        }
    }

    @Override
    public boolean addPhotoToDocument(@NotNull Document document, Document photo) {

        if (!documentRepository.existsById(document.getId())) {
            return false;
        }

        documentRepository.addPhotoToDocument(photo.getId(), document);
        return true;
    }

    private void validateDocument(Document document) {
        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Document> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja dokumentu nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
