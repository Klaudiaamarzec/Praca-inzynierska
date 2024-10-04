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

    // Check if exist
    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM physicallocations pl
    WHERE (pl.isoriginal = :isOriginal)
    AND (:condition IS NULL OR lower(pl.condition) = lower(:condition))
    AND (:type IS NULL OR lower(pl.type) = lower(:type))
    AND (:description IS NULL OR lower(pl.description) = lower(:description))
    AND (pl.idphys = :physicalId)
    AND (pl.localaddressid = :localAddressId)
    AND (pl.user = :userId)
""", nativeQuery = true)
    boolean existsPhysicalLocation(
            @Param("isOriginal") Boolean isOriginal,
            @Param("condition") String condition,
            @Param("type") String type,
            @Param("description") String description,
            @Param("physicalId") Long physicalId,
            @Param("localAddressId") Long localAddressId,
            @Param("userId") Long userId
    );
}
