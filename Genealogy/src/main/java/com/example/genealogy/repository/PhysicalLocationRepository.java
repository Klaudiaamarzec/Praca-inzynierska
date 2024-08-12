package com.example.genealogy.repository;

import com.example.genealogy.model.PhysicalLocations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalLocationRepository extends JpaRepository<PhysicalLocations, Long>{

    // Find physical localizations based on the owner
    @Query("SELECT p FROM PhysicalLocations p WHERE p.user.id = :userId")
    List<PhysicalLocations> findAllByUserId(@Param("userId") long userId);

    // Find all original physical localizations
    @Query("SELECT p FROM PhysicalLocations p WHERE p.isOriginal = true")
    List<PhysicalLocations> findOriginal();

    // Find all not original physical localizations
    @Query("SELECT p FROM PhysicalLocations p WHERE p.isOriginal = false")
    List<PhysicalLocations> findNotOriginal();
}
