package com.example.genealogy.repository;

import com.example.genealogy.model.Address;
import com.example.genealogy.model.LocalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalAddressRepository extends JpaRepository<LocalAddress, Long>{

    // Find local addresses by country
    @Query(value = "SELECT * FROM LocalAddress l " +
            "WHERE unaccent(lower(l.country)) LIKE unaccent(lower(concat('%', :country, '%')))",
            nativeQuery = true)
    List<LocalAddress> findLocalAddressByCountry(@Param("country") String country);

    // Find local addresses by voivodeship
    @Query(value = "SELECT * FROM LocalAddress l " +
            "WHERE unaccent(lower(l.voivodeship)) LIKE unaccent(lower(concat('%', :voivodeship, '%')))",
            nativeQuery = true)
    List<LocalAddress> findLocalAddressByVoivodeship(@Param("voivodeship") String voivodeship);

    // Find local addresses by city
    @Query(value = "SELECT * FROM LocalAddress l " +
            "WHERE unaccent(lower(l.city)) LIKE unaccent(lower(concat('%', :city, '%')))",
            nativeQuery = true)
    List<LocalAddress> findLocalAddressByCity(@Param("city") String city);

    // Find local addresses by country and city
    @Query(value = "SELECT * FROM LocalAddress l " +
            "WHERE unaccent(lower(l.country)) LIKE unaccent(lower(CONCAT('%', :country, '%'))) " +
            "OR unaccent(lower(l.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "ORDER BY (CASE WHEN unaccent(lower(l.country)) LIKE unaccent(lower(CONCAT('%', :country, '%'))) " +
            "AND unaccent(lower(l.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) THEN 1 ELSE 2 END)",
            nativeQuery = true)
    List<LocalAddress> findByCountryAndCity(@Param("country") String country, @Param("city") String city);

    // Check if already exist in database
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM LocalAddress l " +
            "WHERE unaccent(lower(l.country)) LIKE unaccent(lower(CONCAT('%', :country, '%'))) " +
            "AND unaccent(lower(l.voivodeship)) LIKE unaccent(lower(CONCAT('%', :voivodeship, '%'))) " +
            "AND unaccent(lower(l.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "AND unaccent(lower(l.address)) LIKE unaccent(lower(CONCAT('%', :address, '%')))", nativeQuery = true)
    boolean addressExists(@Param("country") String country, @Param("voivodeship") String voivodeship, @Param("city") String city, @Param("address") String address);

    // Return address
    @Query(value = "SELECT * FROM LocalAddress l " +
            "WHERE unaccent(lower(l.country)) LIKE unaccent(lower(CONCAT('%', :country, '%'))) " +
            "AND unaccent(lower(l.voivodeship)) LIKE unaccent(lower(CONCAT('%', :voivodeship, '%'))) " +
            "AND unaccent(lower(l.city)) LIKE unaccent(lower(CONCAT('%', :city, '%'))) " +
            "AND unaccent(lower(l.address)) LIKE unaccent(lower(CONCAT('%', :address, '%')))", nativeQuery = true)
    List<LocalAddress> getAddress(@Param("country") String country,
                             @Param("voivodeship") String voivodeship,
                             @Param("city") String city,
                             @Param("address") String address);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM LocalAddress l " +
            "WHERE (l.country = :country OR (:country IS NULL AND l.country IS NULL)) " +
            "AND (l.voivodeship = :voivodeship OR (:voivodeship IS NULL AND l.voivodeship IS NULL)) " +
            "AND (l.community = :community OR (:community IS NULL AND l.community IS NULL)) " +
            "AND (l.city = :city OR (:city IS NULL AND l.city IS NULL)) " +
            "AND (l.address = :address OR (:address IS NULL AND l.address IS NULL)) " +
            "AND (l.postalCode = :postalCode OR (:postalCode IS NULL AND l.postalCode IS NULL)) ",
            nativeQuery = true)
    boolean existsLocalAddress(@Param("country") String country,
                               @Param("voivodeship") String voivodeship,
                               @Param("community") String community,
                               @Param("city") String city,
                               @Param("address") String address,
                               @Param("postalCode") String postalCode);

    // Return local addresses by all parameters
    @Query(value = "SELECT * FROM localaddress la " +
            "WHERE (la.country = :country OR (:country IS NULL AND la.country IS NULL)) " +
            "AND (la.voivodeship = :voivodeship OR (:voivodeship IS NULL AND la.voivodeship IS NULL)) " +
            "AND (la.community = :community OR (:community IS NULL AND la.community IS NULL)) " +
            "AND (la.city = :city OR (:city IS NULL AND la.city IS NULL)) " +
            "AND (la.address = :address OR (:address IS NULL AND la.address IS NULL)) " +
            "AND (la.postalCode = :postalCode OR (:postalCode IS NULL AND la.postalCode IS NULL)) ",
            nativeQuery = true)
    LocalAddress getLocalAddressByAllParams(
            @Param("country") String country,
            @Param("voivodeship") String voivodeship,
            @Param("community") String community,
            @Param("city") String city,
            @Param("address") String address,
            @Param("postalCode") String postalCode
    );
}
