package com.example.genealogy.service;

import com.example.genealogy.model.LocalAddress;

import java.util.List;

public interface LocalAddressService {

    boolean saveLocalAddress(LocalAddress localAddress);

    boolean updateLocalAddress(LocalAddress localAddress);

    boolean existsById(long id);

    List<LocalAddress> getAllLocalAddresses();

    boolean deleteLocalAddress(LocalAddress localAddress);

    List<LocalAddress> findLocalAddressByCountry(String country);

    List<LocalAddress> findLocalAddressByVoivodeship(String voivodeship);

    List<LocalAddress> findLocalAddressByCity(String city);

    List<LocalAddress> findByCountryAndCity(String country, String city);

    boolean addressExists(String country, String voivodeship, String city, String address);

    List<LocalAddress> getAddress(String country, String voivodeship, String city, String address);
}
