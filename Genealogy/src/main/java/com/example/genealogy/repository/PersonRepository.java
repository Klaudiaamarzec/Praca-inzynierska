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
    List<Object[]> getPersonList();

    // Search person by parameter
    @Query(value = "SELECT * FROM Person p " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(p.surname)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) ",
            nativeQuery = true)
    List<Person> findPersonByParameter(@Param("parameter") String parameter);

    // Find all persons in a specific document
    @Query("SELECT p FROM Person p JOIN p.personDocuments pd WHERE pd.document.id = :documentId")
    List<Person> findAllPersonsInDocument(@Param("documentId") long documentId);
}
