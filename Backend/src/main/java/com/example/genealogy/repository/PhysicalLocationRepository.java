package com.example.genealogy.repository;

import com.example.genealogy.model.PhysicalLocations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    // Check if exist
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM physicallocations pl " +
     "WHERE (lower(pl.condition) = lower(:condition) OR (:condition IS NULL AND lower(pl.condition) IS NULL)) " +
     "AND (lower(pl.type) = lower(:type) OR (:type IS NULL AND lower(pl.type) IS NULL)) " +
     "AND (lower(pl.description) = lower(:description) OR (:description IS NULL AND lower(pl.description) IS NULL)) " +
     "AND (pl.idphys = :physicalId OR (:physicalId IS NULL AND pl.idphys IS NULL)) " +
     "AND (pl.localaddressid = :localAddressId OR (:localAddressId IS NULL AND pl.localaddressid IS NULL)) " +
     "AND (pl.userid = :userId OR (:userId IS NULL AND pl.userid IS NULL))",
     nativeQuery = true)
    boolean existsPhysicalLocation(
            @Param("condition") String condition,
            @Param("type") String type,
            @Param("description") String description,
            @Param("physicalId") Long physicalId,
            @Param("localAddressId") Long localAddressId,
            @Param("userId") Long userId
    );
}
