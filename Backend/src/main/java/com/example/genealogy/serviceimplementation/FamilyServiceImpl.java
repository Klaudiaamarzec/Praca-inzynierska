package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;
import com.example.genealogy.repository.FamilyRepository;
import com.example.genealogy.service.FamilyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;
    private final Validator validator;

    public FamilyServiceImpl(FamilyRepository familyRepository, Validator validator) {
        this.familyRepository = familyRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Long id) {
        return familyRepository.existsById(id);
    }

    @Override
    public boolean familyExists(@NotNull Family family) {
        return familyRepository.existsFamily(
                family.getChild() != null ? family.getChild().getId() : null,
                family.getFather() != null ? family.getFather().getId() : null,
                family.getMother() != null ? family.getMother().getId() : null );
    }

    @Override
    public Family getFamilyById(Long id) {
        return familyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rodziny o id: " + id));
    }

    @Override
    public ResponseEntity<String> saveFamily(@NotNull Family family) {
        if (familyExists(family)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rodzina już istnieje");
        }

        validateFamily(family);

        try {
            familyRepository.save(family);
            return ResponseEntity.ok("Rodzina została prawidłowo dodana");
        } catch (DataAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLException) {
                String errorMessage = cause.getMessage();
                // Filtrujemy komunikat, aby uzyskać tylko wiadomość po ERROR:
                if (errorMessage != null && errorMessage.contains("ERROR:")) {
                    // Wyciągamy tylko istotną część komunikatu
                    String filteredMessage = extractRelevantErrorMessage(errorMessage);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(filteredMessage);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd zapisu: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd zapisu: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd: " + e.getMessage());
        }
    }

    @Override
    public boolean updateFamily(@NotNull Family family) {
        if (!existsById(family.getId())) {
            return false;
        }

        validateFamily(family);

        try {
            familyRepository.save(family);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }

    @Override
    public boolean deleteFamily(Family family) {
        try {
            if (existsById(family.getId())) {
                familyRepository.deleteById(family.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Family findFamilyByChild(Person child) {
        return familyRepository.findFamilyByChild(child);
    }

    @Override
    public List<Family> findFamiliesByMother(Person mother) {
        return familyRepository.findFamiliesByMother(mother);
    }

    @Override
    public List<Family> findFamiliesByFather(Person father) {
        return familyRepository.findFamiliesByFather(father);
    }

    @Override
    public List<Family> findFamiliesByParent(Person parent) {
        return familyRepository.findFamiliesByParent(parent);
    }

    private void validateFamily(Family family) {
        Set<ConstraintViolation<Family>> violations = validator.validate(family);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Family> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja rodziny nie powiodła się:\n" + sb, violations);
        }
    }

    private String extractRelevantErrorMessage(String errorMessage) {
        String regex = "Osoba o ID \\d+ jest już.*?i nie może być.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group();
        }
        return "Nieznany błąd";
    }
}
