package com.example.genealogy.repository;

import com.example.genealogy.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Long>{

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM personindocument pd
    WHERE (pd.personid = :personId)
    AND (pd.document = :documentId)
    AND (:comment IS NULL OR unaccent(lower(pd.comment)) LIKE unaccent(lower(CONCAT('%', :comment, '%'))))
    AND (:x IS NULL OR pd.x = :x)
    AND (:y IS NULL OR pd.y = :y)
""", nativeQuery = true)
    boolean existsPersonDocument(
            @Param("personId") Long personId,
            @Param("documentId") Long documentId,
            @Param("comment") String comment,
            @Param("x") Integer x,
            @Param("y") Integer y
    );
}
