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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    public boolean saveFamily(@NotNull Family family) {
        if (familyExists(family)) {
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
}
