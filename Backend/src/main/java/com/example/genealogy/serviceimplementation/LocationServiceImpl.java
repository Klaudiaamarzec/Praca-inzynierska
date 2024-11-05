package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.*;
import com.example.genealogy.repository.LocationRepository;
import com.example.genealogy.service.DocumentService;
import com.example.genealogy.service.LocationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final DocumentService documentService;
    private final Validator validator;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, DocumentService documentService, Validator validator) {
        this.locationRepository = locationRepository;
        this.documentService = documentService;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Long id) {
        return locationRepository.existsById(id);
    }

    @Override
    public boolean locationExists(@NotNull Location location) {
        return locationRepository.existsLocation(location.getPhysical(), location.getUrl());
    }

    @Override
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono lokalizacji o id: " + id));
    }

    @Override
    public boolean saveLocation(@NotNull Location location) {
        if (locationExists(location)) {
            return false;
        }

        validateLocation(location);

        try {
            locationRepository.save(location);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public boolean deleteLocation(Location location) {
        try {
            if (existsById(location.getId())) {
                locationRepository.delete(location);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public ResponseEntity<?> addLocalizationToDocument(Long documentID) {

        Document document = documentService.getDocumentById(documentID);

        System.out.println("Dokument: " + document.getId() + ", " + document.getTitle());

        if(document.getLocalization() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lokalizacja jest już ustawiona dla tego dokumentu");
        }

        Location location = new Location();
        boolean locationResult = saveLocation(location);

        if(!locationResult) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie udało się zapisać nowej lokalizacji w bazie danych");
        }

        // Utworzenie nowych identyfikatorów URL i PhysicalLocation
        location.setPhysical(location.getId());
        location.setUrl(location.getId());

        locationResult = saveLocation(location);

        if(!locationResult) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie udało się zapisać nowej lokalizacji w bazie danych");
        }

        document.setLocalization(location);
        boolean result = documentService.updateDocument(document);

        if(!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie udało się dodać nowej lokalizacji do dokumentu");
        }

        return ResponseEntity.status(HttpStatus.OK).body("{\"locationId\": " + location.getId() + "}");
    }

    @Override
    public List<Location> findByPhysicalID(long physicalID) {
        return locationRepository.findByPhysicalID(physicalID);
    }

    @Override
    public List<Location> findByUrlID(long urlID) {
        return locationRepository.findByUrlID(urlID);
    }

    @Override
    public List<Location> findLocationByDocument(Document document) {
        return locationRepository.findLocationByDocument(document.getId());
    }

    private void validateLocation(Location location) {
        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Location> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja lokalizacji nie powiodła się:\n" + sb, violations);
        }
    }
}
