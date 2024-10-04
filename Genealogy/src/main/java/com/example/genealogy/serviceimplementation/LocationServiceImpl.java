package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.LocalAddress;
import com.example.genealogy.model.Location;
import com.example.genealogy.repository.LocationRepository;
import com.example.genealogy.service.LocationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final Validator validator;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, Validator validator) {
        this.locationRepository = locationRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Location location) {
        return locationRepository.existsById(location.getId());
    }

    @Override
    public boolean locationExist(@NotNull Location location) {
        return locationRepository.existsLocation(location.getPhysical(), location.getUrl());
    }

    @Override
    public boolean saveLocation(@NotNull Location location) {
        if (locationExist(location)) {
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
    public boolean updateLocation(@NotNull Location location) {
        if (!existsById(location)) {
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
            if (existsById(location)) {
                locationRepository.delete(location);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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
    public List<Location> findLocationByDocument(long documentId) {
        return locationRepository.findLocationByDocument(documentId);
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
            throw new ConstraintViolationException("Walidacja lokalizacji nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
