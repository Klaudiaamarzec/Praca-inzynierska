package com.example.genealogy.repository;

import com.example.genealogy.model.PhysicalLocations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalLocationRepository extends JpaRepository<PhysicalLocations, Long>{
}
