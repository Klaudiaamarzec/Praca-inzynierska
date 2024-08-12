package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Location;
import com.example.genealogy.model.PhysicalLocations;
import com.example.genealogy.model.URLs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{

    //Return all confirmed documents
    @Query("SELECT d FROM Document d WHERE d.confirmed = true")
    List<Document> findConfirmedDocuments();

    //Return all confirmed documents for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.confirmed = true")
    List<Document> findConfirmedDocumentsPerson(@Param("name") String name, @Param("surname") String surname);

    //Return all not confirmed documents
    @Query("SELECT d FROM Document d WHERE d.confirmed = false")
    List<Document> findNotConfirmedDocuments();

    // Return all documents for a specific person by their ID
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd WHERE pd.person.id = :personID")
    List<Document> findDocumentsForPerson(@Param("personID") long personID);

    //Return all not confirmed documents for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.confirmed = false")
    List<Document> findNotConfirmedDocumentsPerson(@Param("name") String name, @Param("surname") String surname);

    //Return all documents based on type
    @Query("SELECT d FROM Document d WHERE d.type.id = :typeId")
    List<Document> findDocumentsByType(@Param("typeId") int typeId);

    //Return all documents based on type for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.type.id = :typeId")
    List<Document> findDocumentsPersonByType(@Param("name") String name, @Param("surname") String surname, @Param("typeId") int typeId);

    //Return all photographs
    @Query("SELECT d FROM Document d WHERE d.type.id = 1")
    List<Document> findPhotographs();

    //Return all photographs for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.type.id = 1")
    List<Document> findPersonPhotographs(@Param("name") String name, @Param("surname") String surname);

    // Return all documents based on types
    @Query("SELECT d FROM Document d WHERE d.type.id IN :typeIds")
    List<Document> findDocumentsByTypeIds(@Param("typeIds") List<Long> typeIds);

    // Return all documents based on types for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.type.id IN :typeIds")
    List<Document> findDocumentsPersonByTypeIds(@Param("name") String name, @Param("surname") String surname, @Param("typeIds") List<Long> typeIds);

    // Return all documents based on date range
    @Query("SELECT d FROM Document d WHERE d.startDate >= :fromDate AND d.endDate <= :toDate")
    List<Document> findDocumentsByDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    // Return all documents based on date range for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.startDate >= :fromDate AND d.endDate <= :toDate")
    List<Document> findDocumentsPersonByDateRange(@Param("name") String name, @Param("surname") String surname, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    // Return all documents based on date
    @Query("SELECT d FROM Document d WHERE d.date.id = :date")
    List<Document> findDocumentsByDate(@Param("date") long date);

    // Return all documents based on date for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.date.id = :date")
    List<Document> findDocumentsPersonByDate(@Param("name") String name, @Param("surname") String surname, @Param("date") long date);

    // Return all documents based on place
    @Query("SELECT d FROM Document d WHERE d.place.id = :placeId")
    List<Document> findDocumentsByPlace(@Param("placeId") long placeId);

    // Return all documents based on place for person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.place.id = :placeId")
    List<Document> findDocumentsPersonByPlace(@Param("name") String name, @Param("surname") String surname, @Param("placeId") long placeId);

    // Return all documents based on the owner
    @Query("SELECT d FROM Document d WHERE d.owner.id = :ownerID")
    List<Document> findDocumentsByOwner(@Param("ownerID") long ownerID);

    // Find documents locations
    //@Query("SELECT l FROM Location l WHERE l.id = (SELECT d.localization.id FROM Document d WHERE d.id = :documentId)")
    //List<Location> findDocumentLocations(@Param("documentId") long documentId);

    // Find documents based on localization
    @Query("SELECT d FROM Document d WHERE d.localization = :localizationID")
    List<Document> findDocumentsByLocalization(@Param("localizationID") long localizationID);

    // Find documents based on localization for Person
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.localization = :localizationID")
    List<Document> findDocumentsPersonByLocalization(@Param("name") String name, @Param("surname") String surname, @Param("localizationID") long localizationID);

    // Find document physical localizations
    //@Query("SELECT p FROM PhysicalLocations p WHERE p.id IN (SELECT l.physicalLocations FROM Location l WHERE l.id = (SELECT d.localization.id FROM Document d WHERE d.id = :documentId))")
    //List<PhysicalLocations> findDocumentPhysicalLocations(@Param("documentId") long documentId);

    // Find document URLs
    //@Query("SELECT u FROM URLs u WHERE u.id IN (SELECT l.urls FROM Location l WHERE l.id = (SELECT d.localization.id FROM Document d WHERE d.id = :documentId))")
    //List<URLs> findDocumentURLs(@Param("documentId") long documentId);

    // Add photography to existing document
    @Modifying
    @Transactional
    @Query("UPDATE Document d SET d.photography = :photo WHERE d.id = :documentId")
    void addPhotoToDocument(@Param("documentId") long documentId, @Param("photo") Document photo);

    // Add photographs to existing document
    @Modifying
    @Transactional
    @Query("UPDATE Document d SET d.photos = :photos WHERE d.id = :documentId")
    void addPhotosToDocument(@Param("documentId") long documentId, @Param("photos") Set<Document> photos);

}
