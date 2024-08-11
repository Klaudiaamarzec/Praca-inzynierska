package com.example.genealogy.repository;

import com.example.genealogy.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Long>{
}
