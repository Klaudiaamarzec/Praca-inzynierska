package com.example.genealogy.repository;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    // Find all families where Person is child
    @Query("SELECT f FROM Family f WHERE f.child = :person")
    List<Family> findFamiliesByChild(@Param("person") Person person);

    // Find all families where Person is mother
    @Query("SELECT f FROM Family f WHERE f.mother = :person")
    List<Family> findFamiliesByMother(@Param("person") Person person);

    // Find all families where Person is father
    @Query("SELECT f FROM Family f WHERE f.father = :person")
    List<Family> findFamiliesByFather(@Param("person") Person person);

    // Find all families where Person is a parent
    @Query("SELECT f FROM Family f WHERE f.father = :person OR f.mother = :person")
    List<Family> findFamiliesByParent(@Param("person") Person person);

    // Check if exist
    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM family f
        WHERE (f.cid = :childId)
        AND (f.fid = :fatherId)
        AND (f.mid = :motherId)
    """, nativeQuery = true)
    boolean existsFamily(@Param("childId") Long childId,
                         @Param("fatherId") Long fatherId,
                         @Param("motherId") Long motherId);
}
