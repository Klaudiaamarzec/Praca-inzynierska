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
    @Query("SELECT l FROM LocalAddress l WHERE l.country = :country")
    List<LocalAddress> findLocalAddressByCountry(@Param("country") String country);

    // Find local addresses by voivodeship
    @Query("SELECT l FROM LocalAddress l WHERE l.voivodeship = :voivodeship")
    List<LocalAddress> findLocalAddressByVoivodeship(@Param("voivodeship") String voivodeship);

    // Find local addresses by city
    @Query("SELECT l FROM LocalAddress l WHERE l.city = :city")
    List<LocalAddress> findLocalAddressByCity(@Param("city") String city);

    // Find local addresses by country and city
    @Query("SELECT l FROM LocalAddress l WHERE l.country = :country AND l.city = :city")
    List<Address> findByParishAndCity(@Param("country") String country, @Param("city") String city);

    // Check if already exist in database
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM LocalAddress l WHERE l.country = :country AND l.voivodeship = :voivodeship AND l.city = :city AND l.address = :address")
    boolean exists(@Param("country") String country, @Param("voivodeship") String voivodeship, @Param("city") String city, @Param("address") String address);

    // Return address
    @Query("SELECT l FROM LocalAddress l WHERE l.country = :country AND l.voivodeship = :voivodeship AND l.city = :city AND l.address = :address")
    List<Address> getAddress(@Param("country") String country,
                             @Param("voivodeship") String voivodeship,
                             @Param("city") String city,
                             @Param("address") String address);
}
