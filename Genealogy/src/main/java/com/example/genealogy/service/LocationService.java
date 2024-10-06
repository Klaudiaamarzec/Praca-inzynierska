package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.LocalAddress;
import com.example.genealogy.model.Location;

import java.util.List;

public interface LocationService {

    boolean existsById(Long id);

    boolean locationExists(Location location);

    Location getLocationById(Long id);

    boolean saveLocation(Location location);

    boolean deleteLocation(Location location);

    List<Location> getAllLocations();

    List<Location> findByPhysicalID(long physicalID);

    List<Location> findByUrlID(long urlID);

    List<Location> findLocationByDocument(Document document);
}
