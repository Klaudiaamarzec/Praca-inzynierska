package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Long>{

    // Find all documents for Person
    @Query("SELECT pd.document FROM PersonDocument pd WHERE pd.person.id = :personId")
    List<Document> findDocumentsForPerson(@Param("personId") long personId);

    // Find all people in Document
    @Query("SELECT pd.person FROM PersonDocument pd WHERE pd.document.id = :documentId")
    List<Person> findPeopleInDocument(@Param("documentId") long documentId);
}
