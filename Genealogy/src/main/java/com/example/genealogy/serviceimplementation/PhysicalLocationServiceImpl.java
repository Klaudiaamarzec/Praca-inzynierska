package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.PhysicalLocations;
import com.example.genealogy.model.User;
import com.example.genealogy.repository.PhysicalLocationRepository;
import com.example.genealogy.service.PhysicalLocationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PhysicalLocationServiceImpl implements PhysicalLocationService {

    private final PhysicalLocationRepository physicalLocationRepository;
    private final Validator validator;

    @Autowired
    public PhysicalLocationServiceImpl(PhysicalLocationRepository physicalLocationRepository, Validator validator) {
        this.physicalLocationRepository = physicalLocationRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull PhysicalLocations physicalLocation) {
        return physicalLocationRepository.existsById(physicalLocation.getId());
    }

    @Override
    public boolean physicalLocationExist(@NotNull PhysicalLocations physicalLocation) {
        return physicalLocationRepository.existsPhysicalLocation(physicalLocation.isOriginal(), physicalLocation.getCondition(), physicalLocation.getType(), physicalLocation.getDescription(), physicalLocation.getPhysical().getId(), physicalLocation.getLocaladdress().getId(), physicalLocation.getUser().getId());
    }

    @Override
    public boolean savePhysicalLocation(@NotNull PhysicalLocations physicalLocation) {
        if (physicalLocationExist(physicalLocation)) {
            return false;
        }

        validatePhysicalLocation(physicalLocation);

        try {
            physicalLocationRepository.save(physicalLocation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updatePhysicalLocation(@NotNull PhysicalLocations physicalLocation) {
        if (!existsById(physicalLocation)) {
            return false;
        }

        validatePhysicalLocation(physicalLocation);

        try {
            physicalLocationRepository.save(physicalLocation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deletePhysicalLocation(PhysicalLocations physicalLocation) {
        try {
            if (existsById(physicalLocation)) {
                physicalLocationRepository.delete(physicalLocation);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<PhysicalLocations> getAllPhysicalLocations() {
        return physicalLocationRepository.findAll();
    }

    @Override
    public List<PhysicalLocations> findAllByUser(@NotNull User user) {
        return physicalLocationRepository.findAllByUserId(user.getId());
    }

    @Override
    public List<PhysicalLocations> findOriginal() {
        return physicalLocationRepository.findOriginal();
    }

    @Override
    public List<PhysicalLocations> findNotOriginal() {
        return physicalLocationRepository.findNotOriginal();
    }

    private void validatePhysicalLocation(PhysicalLocations physicalLocation) {
        Set<ConstraintViolation<PhysicalLocations>> violations = validator.validate(physicalLocation);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<PhysicalLocations> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja lokalizacji fizycznej nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
