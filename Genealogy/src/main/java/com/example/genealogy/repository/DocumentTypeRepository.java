package com.example.genealogy.repository;

import com.example.genealogy.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer>{

    // Find if exist
    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM documenttype dt
        WHERE (:typeName IS NULL OR unaccent(lower(dt.typename)) = unaccent(lower(:typeName)))
        AND (:template IS NULL OR unaccent(lower(dt.template)) = unaccent(lower(:template)))
    """, nativeQuery = true)
    boolean existsDocumentType(@Param("typeName") String typeName,
                               @Param("template") String template);
}
