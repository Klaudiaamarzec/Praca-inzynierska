package com.example.genealogy.service;

import com.example.genealogy.model.LocalAddress;

import java.util.List;

public interface LocalAddressService {

    boolean existsById(Long id);

    boolean localAddressExists(LocalAddress localAddress);

    LocalAddress getLocalAddressById(Long id);

    boolean saveLocalAddress(LocalAddress localAddress);

    boolean updateLocalAddress(LocalAddress localAddress);

    boolean deleteLocalAddress(LocalAddress localAddress);

    List<LocalAddress> getAllLocalAddresses();

    List<LocalAddress> findLocalAddressByCountry(String country);

    List<LocalAddress> findLocalAddressByVoivodeship(String voivodeship);

    List<LocalAddress> findLocalAddressByCity(String city);

    List<LocalAddress> searchLocalAddress(String country, String voivodeship, String city);
}
