package com.example.genealogy.service;

import com.example.genealogy.model.PhysicalLocations;
import com.example.genealogy.model.User;

import java.util.List;

public interface PhysicalLocationService {

    boolean savePhysicalLocation(PhysicalLocations physicalLocation);

    boolean updatePhysicalLocation(PhysicalLocations physicalLocation);

    boolean existsById(long id);

    boolean deletePhysicalLocation(PhysicalLocations physicalLocation);

    List<PhysicalLocations> getAllPhysicalLocations();

    List<PhysicalLocations> findAllByUser(User user);

    List<PhysicalLocations> findOriginal();

    List<PhysicalLocations> findNotOriginal();
}
