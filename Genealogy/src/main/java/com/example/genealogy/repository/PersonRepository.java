package com.example.genealogy.repository;

import com.example.genealogy.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

    // Get simple person list
    @Query("SELECT p.name, p.surname FROM Person p")
    List<Person> getPersonList();

    // Search person by parameter
    @Query("SELECT p FROM Person p WHERE p.name LIKE %:parameter% OR p.surname LIKE %:parameter%")
    List<Person> findPersonByParameter(@Param("parameter") String parameter);

    // Find all persons in a specific document
    @Query("SELECT p FROM Person p JOIN p.personDocuments pd WHERE pd.document.id = :documentId")
    List<Person> findAllPersonsInDocument(@Param("documentId") long documentId);
}
