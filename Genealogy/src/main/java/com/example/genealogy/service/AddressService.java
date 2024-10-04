package com.example.genealogy.service;

import com.example.genealogy.model.Address;

import java.util.List;

public interface AddressService {

    boolean existsById(Address address);

    // My  method from repository
    boolean addressExists(Address address);

    // Save = add or update an address
    boolean saveAddress(Address address);

    boolean updateAddress(Address address);

    boolean deleteAddress(Address address);

    List<Address> getAllAddresses();

    List<Address> findByCountry(String country);

    List<Address> findByVoivodeship(String voivodeship);

    List<Address> findByCommunity(String community);

    List<Address> findByCity(String city);

    List<Address> findByAddress(String address);

    List<Address> findByPostalCode(String postalCode);

    List<Address> findByParish(String parish);

    List<Address> findByCoordinates(Long longitude, Long latitude);

    List<Address> findByLongitudeBetween(Long minLongitude, Long maxLongitude);

    List<Address> findByLatitudeBetween(Long minLatitude, Long maxLatitude);

    List<Address> searchAddress(String country, String voivodeship, String community, String city, String address, String postalCode, String parish, Long longitude, Long latitude, Long minLongitude, Long maxLongitude, Long minLatitude, Long maxLatitude);

    List<Address> findByCityAndVoivodeship(String city, String voivodeship);

    List<Address> findByCountryAndPostalCode(String country, String postalCode);

    List<Address> findByParishAndCity(String parish, String city);

    List<Address> findByPostalCodeBetween(String startPostalCode, String endPostalCode);

    List<Address> getAddress(String country, String voivodeship, String city, String address);

    List<Address> getAddressesByParam(String parameter);

    List<Address> getAddressesByAllParams(String country, String voivodeship, String community, String city, Long longitude, Long latitude, String address, String postalCode, String parish, String secular);
}
