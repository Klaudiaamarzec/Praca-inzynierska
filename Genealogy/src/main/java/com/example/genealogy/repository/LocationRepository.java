package com.example.genealogy.repository;

import com.example.genealogy.model.Location;
import com.example.genealogy.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

    // Search locations based on Physical Location
    @Query("SELECT l FROM Location l WHERE l.physical = :physicalID")
    List<Location> findByPhysicalID(@Param("physicalID") long physicalID);

    // Search locations based on URLs
    @Query("SELECT l FROM Location l JOIN l.urls u WHERE u.id = :url")
    List<Location> findByUrl(@Param("url") long url);

    // Find all locations for a given document
    @Query("SELECT l FROM Location l WHERE l.id = (SELECT d.localization.id FROM Document d WHERE d.id = :documentId)")
    List<Location> findLocationByDocument(@Param("documentId") long documentId);

}