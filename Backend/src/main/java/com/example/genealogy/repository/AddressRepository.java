package com.example.genealogy.repository;

import java.util.List;
import com.example.genealogy.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Searching addresses based on the country
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.country)) LIKE unaccent(lower(concat('%', :country, '%')))",
            nativeQuery = true)
    List<Address> findByCountry(@Param("country") String country);

    // Searching addresses based on the voivodeship
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.voivodeship)) LIKE unaccent(lower(concat('%', :voivodeship, '%')))",
            nativeQuery = true)
    List<Address> findByVoivodeship(@Param("voivodeship") String voivodeship);

    // Searching addresses based on the community
    @Query(value = "SELECT * FROM Address a " +
            "WHERE unaccent(lower(a.community)) LIKE unaccent(lower(CONCAT('%', :community, '%')))", nativeQuery = true)
    List<Address> findByCommunity(@Param("community") String community);

    // Searching addresses based on the city
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.city)) LIKE unaccent(lower(concat('%', :city, '%')))",
            nativeQuery = true)
    List<Address> findByCity(@Param("city") String city);

    // Searching addresses based on the specific address
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.address)) LIKE unaccent(lower(CONCAT('%', :address, '%')))",
            nativeQuery = true)
    List<Address> findByAddress(@Param("address") String address);

    // Searching addresses based on the postal code
    @Query("SELECT a FROM Address a WHERE a.postalCode = :postalCode")
    List<Address> findByPostalCode(@Param("postalCode") String postalCode);

    // Searching addresses based on the parish
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.parish)) LIKE unaccent(lower(CONCAT('%', :parish, '%')))",
            nativeQuery = true)
    List<Address> findByParish(@Param("parish") String parish);

    // Searching addresses based on coordinates
    @Query("SELECT a FROM Address a WHERE a.longitude = :longitude AND a.latitude = :latitude")
    List<Address> findByCoordinates(@Param("longitude") Double longitude, @Param("latitude") Double latitude);

    // Searching addresses based on coordinates ( longitude )
    @Query("SELECT a FROM Address a WHERE a.longitude BETWEEN :minLongitude AND :maxLongitude")
    List<Address> findByLongitudeBetween(@Param("minLongitude") Double minLongitude, @Param("maxLongitude") Double maxLongitude);

    // Searching addresses based on coordinates (latitude)
    @Query("SELECT a FROM Address a WHERE a.latitude BETWEEN :minLatitude AND :maxLatitude")
    List<Address> findByLatitudeBetween(@Param("minLatitude") Double minLatitude, @Param("maxLatitude") Double maxLatitude);

    // Searching addresses based on city and voivodeship
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "OR unaccent(lower(a.voivodeship)) LIKE unaccent(lower(CONCAT('%', :voivodeship, '%'))) " +
            "ORDER BY (CASE WHEN unaccent(lower(a.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "AND unaccent(lower(a.voivodeship)) LIKE unaccent(lower(CONCAT('%', :voivodeship, '%'))) THEN 1 ELSE 2 END)",
            nativeQuery = true)
    List<Address> findByCityAndVoivodeship(@Param("city") String city, @Param("voivodeship") String voivodeship);

    // WSearching addresses based on country and postal code
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.country)) LIKE unaccent(lower(CONCAT('%', :country, '%'))) " +
            "AND a.postalcode = :postalCode",
            nativeQuery = true)
    List<Address> findByCountryAndPostalCode(@Param("country") String country, @Param("postalCode") String postalCode);

    // Searching addresses based on parish and city
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.parish)) LIKE unaccent(lower(CONCAT('%', :parish, '%'))) " +
            "OR unaccent(lower(a.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "ORDER BY (CASE WHEN unaccent(lower(a.parish)) LIKE unaccent(lower(CONCAT('%', :parish, '%'))) " +
            "AND unaccent(lower(a.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) THEN 1 ELSE 2 END)",
            nativeQuery = true)
    List<Address> findByParishAndCity(@Param("parish") String parish, @Param("city") String city);

    // Searching addresses based on postal code
    @Query("SELECT a FROM Address a WHERE a.postalCode BETWEEN :startPostalCode AND :endPostalCode")
    List<Address> findByPostalCodeBetween(@Param("startPostalCode") String startPostalCode, @Param("endPostalCode") String endPostalCode);

    // Check if already exist in database
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM address a " +
            "WHERE (a.country = :country OR (:country IS NULL AND a.country IS NULL)) " +
            "AND (a.voivodeship = :voivodeship OR (:voivodeship IS NULL AND a.voivodeship IS NULL)) " +
            "AND (a.community = :community OR (:community IS NULL AND a.community IS NULL)) " +
            "AND (a.city = :city OR (:city IS NULL AND a.city IS NULL)) " +
            "AND (a.address = :address OR (:address IS NULL AND a.address IS NULL)) " +
            "AND (a.postalCode = :postalCode OR (:postalCode IS NULL AND a.postalCode IS NULL)) " +
            "AND (a.parish = :parish OR (:parish IS NULL AND a.parish IS NULL)) " +
            "AND (a.longitude = :longitude OR (:longitude IS NULL AND a.longitude IS NULL)) " +
            "AND (a.latitude = :latitude OR (:latitude IS NULL AND a.latitude IS NULL)) " +
            "AND (a.secular = :secular OR (:secular IS NULL AND a.secular IS NULL))",
            nativeQuery = true)
    boolean addressExists(@Param("country") String country,
                          @Param("voivodeship") String voivodeship,
                          @Param("community") String community,
                          @Param("city") String city,
                          @Param("address") String address,
                          @Param("postalCode") String postalCode,
                          @Param("parish") String parish,
                          @Param("longitude") Double longitude,
                          @Param("latitude") Double latitude,
                          @Param("secular") String secular);

    // Return address
    @Query(value = "SELECT * FROM Address a " +
            "WHERE unaccent(lower(a.country)) LIKE unaccent(lower(CONCAT('%', :country, '%'))) " +
            "AND unaccent(lower(a.voivodeship)) LIKE unaccent(lower(CONCAT('%', :voivodeship, '%'))) " +
            "AND unaccent(lower(a.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "AND unaccent(lower(a.address)) LIKE unaccent(lower(CONCAT('%', :address, '%')))", nativeQuery = true)
    List<Address> getAddress(@Param("country") String country,
                       @Param("voivodeship") String voivodeship,
                       @Param("city") String city,
                       @Param("address") String address);

    // Return adresses by parametr
    @Query(value = "SELECT * FROM address a " +
            "WHERE unaccent(lower(a.country)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(a.voivodeship)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(a.city)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(a.address)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(a.community)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(a.parish)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(a.postalCode)) LIKE unaccent(lower(CONCAT('%', :parameter, '%')))",
            nativeQuery = true)
    List<Address> getAddressesByParam(@Param("parameter") String parameter);

    // Return addresses by all parameters

@Query(value = "SELECT * FROM address a " +
        "WHERE (a.country = :country OR (:country IS NULL AND a.country IS NULL)) " +
        "AND (a.voivodeship = :voivodeship OR (:voivodeship IS NULL AND a.voivodeship IS NULL)) " +
        "AND (a.community = :community OR (:community IS NULL AND a.community IS NULL)) " +
        "AND (a.city = :city OR (:city IS NULL AND a.city IS NULL)) " +
        "AND (a.longitude = :longitude OR (:longitude IS NULL AND a.longitude IS NULL)) " +
        "AND (a.latitude = :latitude OR (:latitude IS NULL AND a.latitude IS NULL)) " +
        "AND (a.address = :address OR (:address IS NULL AND a.address IS NULL)) " +
        "AND (a.postalCode = :postalCode OR (:postalCode IS NULL AND a.postalCode IS NULL)) " +
        "AND (a.parish = :parish OR (:parish IS NULL AND a.parish IS NULL)) " +
        "AND (a.secular = :secular OR (:secular IS NULL AND a.secular IS NULL))",
        nativeQuery = true)
    Address getAddressByAllParams(
            @Param("country") String country,
            @Param("voivodeship") String voivodeship,
            @Param("community") String community,
            @Param("city") String city,
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude,
            @Param("address") String address,
            @Param("postalCode") String postalCode,
            @Param("parish") String parish,
            @Param("secular") String secular
    );

}
