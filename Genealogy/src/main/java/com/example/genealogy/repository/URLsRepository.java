package com.example.genealogy.repository;

import com.example.genealogy.model.URLs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLsRepository extends JpaRepository<URLs, Long>{
}
