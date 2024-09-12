package com.example.genealogy.service;

import com.example.genealogy.model.Location;

import java.util.List;

public interface LocationService {

    boolean saveLocation(Location location);

    boolean updateLocation(Location location);

    boolean existsById(long id);

    List<Location> getAllLocations();

    boolean deleteLocation(Location location);

    List<Location> findByPhysicalID(long physicalID);

    List<Location> findByUrlID(long urlID);

    List<Location> findLocationByDocument(long documentId);
}
