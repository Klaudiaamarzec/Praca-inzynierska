package com.example.genealogy.repository;

import com.example.genealogy.model.Person;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query("""
    SELECT COUNT(p) > 0 FROM Person p
    WHERE (:name IS NULL OR lower(p.name) LIKE lower(CONCAT('%', :name, '%')))
    AND (:surname IS NULL OR lower(p.surname) LIKE lower(CONCAT('%', :surname, '%')))
    AND (:rin IS NULL OR p.rin = :rin)
    AND (:birthdate IS NULL OR p.birthDate = CAST(:birthdate AS date))
""")
    boolean existsPerson(@Param("name") String name,
                         @Param("surname") String surname,
                         @Param("rin") @Nullable Long rin,
                         @Param("birthdate") @Nullable LocalDate birthdate);
}
