package com.example.genealogy.service;

import com.example.genealogy.model.Address;

import java.util.List;

public interface AddressService {

    boolean existsById(Long id);

    // My  method from repository
    boolean addressExists(Address address);

    Address getAddressById(Long id);

    // Save = add or update an address
    boolean saveAddress(Address address);

    boolean updateAddress(Address address);

    boolean deleteAddress(Address address);

    List<Address> getAllAddresses();

    Address getAddressByAllParams(String country, String voivodeship, String community, String city, Double longitude, Double latitude, String address, String postalCode, String parish, String secular);

    List<Address> findByCountry(String country);

    List<Address> findByVoivodeship(String voivodeship);

    List<Address> findByCommunity(String community);

    List<Address> findByCity(String city);

    List<Address> findByAddress(String address);

    List<Address> findByPostalCode(String postalCode);

    List<Address> findByParish(String parish);

    List<Address> findByCoordinates(Double longitude, Double latitude);

    List<Address> findByLongitudeBetween(Double minLongitude, Double maxLongitude);

    List<Address> findByLatitudeBetween(Double minLatitude, Double maxLatitude);

    List<Address> findByPostalCodeBetween(String startPostalCode, String endPostalCode);

    List<Address> searchAddress(String country, String voivodeship, String community, String city, String address, String postalCode, String startPostalCode, String endPostalCode, String parish, Double longitude, Double latitude, Double minLongitude, Double maxLongitude, Double minLatitude, Double maxLatitude);
}
