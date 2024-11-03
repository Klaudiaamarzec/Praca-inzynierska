package com.example.genealogy.repository;

import com.example.genealogy.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Long>{

    @Query("SELECT pd FROM PersonDocument pd WHERE pd.person.id = :personId AND pd.document.id = :documentId")
    PersonDocument getPersonDocument(@Param("documentId") Long documentId, @Param("personId") Long personId);

    @Query("SELECT pd FROM  PersonDocument pd WHERE pd.document.id = :documentId")
    List<PersonDocument> findPersonDocumentByDocumentID(@Param("documentId") Long documentId);

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM personindocument pd
    WHERE (pd.personid = :personId)
    AND (pd.document = :documentId)
""", nativeQuery = true)
    boolean existsPersonDocument(
            @Param("personId") Long personId,
            @Param("documentId") Long documentId
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonDocument pd WHERE pd.document.id = :documentId")
    void deleteByDocumentId(@Param("documentId") Long documentId);
}
