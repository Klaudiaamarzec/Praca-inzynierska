package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{

    //Return all confirmed documents
    @Query("SELECT d FROM Document d WHERE d.confirmed = true")
    List<Document> findConfirmedDocuments();

    //Return all confirmed documents for person
    //@Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.confirmed = true")
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.confirmed = true",
            nativeQuery = true)
    List<Document> findConfirmedDocumentsPerson(@Param("name") String name, @Param("surname") String surname);

    //Return all not confirmed documents
    @Query("SELECT d FROM Document d WHERE d.confirmed = false")
    List<Document> findNotConfirmedDocuments();

    // Return all confirmed documents for a specific person by their ID
    @Query("SELECT d FROM Document d JOIN d.peopleDocuments pd WHERE pd.person.id = :personID AND d.confirmed = true")
    List<Document> findDocumentsForPerson(@Param("personID") long personID);

    //Return all documents based on type
    @Query("SELECT d FROM Document d WHERE d.type.id = :typeId")
    List<Document> findDocumentsByType(@Param("typeId") int typeId);

    //Return all documents based on type for person
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.type = :typeId",
            nativeQuery = true)
    List<Document> findDocumentsPersonByType(@Param("name") String name, @Param("surname") String surname, @Param("typeId") int typeId);

    //Return all photographs
    @Query("SELECT d FROM Document d WHERE d.type.id = 1")
    List<Document> findPhotographs();

    //Return all photographs for person
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.type = 1",
            nativeQuery = true)
    List<Document> findPersonPhotographs(@Param("name") String name, @Param("surname") String surname);

    // Return all documents based on types
    @Query("SELECT d FROM Document d WHERE d.type.id IN :typeIds")
    List<Document> findDocumentsByTypeIds(@Param("typeIds") List<Integer> typeIds);

    // Return all documents based on types for person
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.type IN :typeIds",
            nativeQuery = true)
    List<Document> findDocumentsPersonByTypeIds(@Param("name") String name, @Param("surname") String surname, @Param("typeIds") List<Integer> typeIds);

    // Return all documents based on date range
    @Query("""
    SELECT d FROM Document d
    WHERE (d.startDate <= :toDate AND d.endDate >= :fromDate)
    ORDER BY
        (CASE
            WHEN (d.startDate >= :fromDate AND d.endDate <= :toDate) THEN 1
            ELSE 2
        END),
        d.startDate
""")
    List<Document> findDocumentsByDateRangeStartEnd(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    // Return all documents based on date range for person
    @Query(value = """
    SELECT d.*
    FROM Document d
    JOIN Personindocument pd ON d.id = pd.document
    JOIN Person p ON p.id = pd.personid
    WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%')))
    AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%')))
    AND (d.startdate <= :toDate AND d.enddate >= :fromDate)
    ORDER BY
        CASE
            WHEN (d.startdate >= :fromDate AND d.enddate <= :toDate) THEN 1
            ELSE 2
        END,
        d.startdate
    """, nativeQuery = true)
    List<Document> findDocumentsPersonByDateRangeStartEnd(@Param("name") String name, @Param("surname") String surname, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    // Return all documents based on dates
    @Query("SELECT d FROM Document d WHERE d.date.id IN :dateIds")
    List<Document> findDocumentsByDates(@Param("dateIds") List<Long> dateIds);

    // Return all documents based on dates for person
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.date IN :dateIds",
            nativeQuery = true)
    //@Query("SELECT d FROM Document d JOIN d.peopleDocuments pd JOIN pd.person p WHERE p.name = :name AND p.surname = :surname AND d.date.id IN :date")
    List<Document> findDocumentsPersonByDates(@Param("name") String name, @Param("surname") String surname, @Param("dateIds") List<Long> dateIds);

    // Return all documents based on places
    @Query("SELECT d FROM Document d WHERE d.place.id IN :placeIds")
    List<Document> findDocumentsByPlaces(@Param("placeIds") List<Long> placeIds);

    // Return all documents based on places for person
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.place IN :placeIds",
            nativeQuery = true)
    List<Document> findDocumentsPersonByPlaces(@Param("name") String name, @Param("surname") String surname, @Param("placeIds") List<Long> placeIds);

    // Return all documents based on the owner
    @Query("SELECT d FROM Document d WHERE d.owner.id = :ownerID")
    List<Document> findDocumentsByOwner(@Param("ownerID") long ownerID);

    // Find documents based on localization
    @Query("SELECT d FROM Document d WHERE d.localization.id IN :localizationIDs")
    List<Document> findDocumentsByLocalizations(@Param("localizationIDs") List<Long> localizationIDs);

    // Find documents based on localization for Person
    @Query(value = "SELECT d.* FROM Document d " +
            "JOIN Personindocument pd ON d.id = pd.document " +
            "JOIN Person p ON p.id = pd.personid " +
            "WHERE unaccent(lower(p.name)) LIKE unaccent(lower(concat('%', :name, '%'))) " +
            "AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('%', :surname, '%'))) " +
            "AND d.localization IN :localizationIDs",
            nativeQuery = true)
    List<Document> findDocumentsPersonByLocalization(@Param("name") String name, @Param("surname") String surname, @Param("localizationIDs") List<Long> localizationIDs);

    // Add photography to existing document
    @Modifying
    @Transactional
    @Query("UPDATE Document d SET d.photoRefers = :photoRefers WHERE d.id = :photo AND d.type.id = 1")
    void addPhotoToDocument(@Param("photo") long photo, @Param("photoRefers") Document photoRefers);

}
