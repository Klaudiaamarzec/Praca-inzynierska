package com.example.genealogy.repository;

import java.util.List;
import com.example.genealogy.model.Address;
import com.example.genealogy.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Searching addresses based on the country
    @Query("SELECT a FROM Address a WHERE a.country = :country")
    List<Address> findByCountry(@Param("country") String country);

    // Searching addresses based on the voivodeship
    @Query("SELECT a FROM Address a WHERE a.voivodeship = :voivodeship")
    List<Address> findByVoivodeship(@Param("voivodeship") String voivodeship);

    // Searching addresses based on the community
    @Query("SELECT a FROM Address a WHERE a.community = :community")
    List<Address> findByCommunity(@Param("community") String community);

    // Searching addresses based on the city
    @Query("SELECT a FROM Address a WHERE a.city = :city")
    List<Address> findByCity(@Param("city") String city);

    // Searching addresses based on the specific address
    @Query("SELECT a FROM Address a WHERE a.address = :address")
    List<Address> findByAddress(@Param("address") String address);

    // Searching addresses based on the postal code
    @Query("SELECT a FROM Address a WHERE a.postalCode = :postalCode")
    List<Address> findByPostalCode(@Param("postalCode") String postalCode);

    // Searching addresses based on the parish
    @Query("SELECT a FROM Address a WHERE a.parish = :parish")
    List<Address> findByParish(@Param("parish") String parish);

    // Searching addresses based on coordinates
    @Query("SELECT a FROM Address a WHERE a.longitude = :longitude AND a.latitude = :latitude")
    List<Address> findByCoordinates(@Param("longitude") long longitude, @Param("latitude") long latitude);

    // Searching addresses based on coordinates ( longitude )
    @Query("SELECT a FROM Address a WHERE a.longitude BETWEEN :minLongitude AND :maxLongitude")
    List<Address> findByLongitudeBetween(@Param("minLongitude") long minLongitude, @Param("maxLongitude") long maxLongitude);

    // Searching addresses based on coordinates (latitude)
    @Query("SELECT a FROM Address a WHERE a.latitude BETWEEN :minLatitude AND :maxLatitude")
    List<Address> findByLatitudeBetween(@Param("minLatitude") long minLatitude, @Param("maxLatitude") long maxLatitude);

    // Searching addresses based on city and voivodeship
    @Query("SELECT a FROM Address a WHERE a.city = :city AND a.voivodeship = :voivodeship")
    List<Address> findByCityAndVoivodeship(@Param("city") String city, @Param("voivodeship") String voivodeship);

    // WSearching addresses based on country and postal code
    @Query("SELECT a FROM Address a WHERE a.country = :country AND a.postalCode = :postalCode")
    List<Address> findByCountryAndPostalCode(@Param("country") String country, @Param("postalCode") String postalCode);

    // Searching addresses based on parish and city
    @Query("SELECT a FROM Address a WHERE a.parish = :parish AND a.city = :city")
    List<Address> findByParishAndCity(@Param("parish") String parish, @Param("city") String city);

    // Searching addresses based on postal code
    @Query("SELECT a FROM Address a WHERE a.postalCode BETWEEN :startPostalCode AND :endPostalCode")
    List<Address> findByPostalCodeBetween(@Param("startPostalCode") String startPostalCode, @Param("endPostalCode") String endPostalCode);

    // Check if already exist in database
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Address a WHERE a.country = :country AND a.voivodeship = :voivodeship AND a.city = :city AND a.address = :address")
    boolean exists(@Param("country") String country, @Param("voivodeship") String voivodeship, @Param("city") String city, @Param("address") String address);

    // Return address
    @Query("SELECT a FROM Address a WHERE a.country = :country AND a.voivodeship = :voivodeship AND a.city = :city AND a.address = :address")
    List<Address> getAddress(@Param("country") String country,
                       @Param("voivodeship") String voivodeship,
                       @Param("city") String city,
                       @Param("address") String address);

}
