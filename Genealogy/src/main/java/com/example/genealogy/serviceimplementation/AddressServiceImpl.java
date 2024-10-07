package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Address;
import com.example.genealogy.repository.AddressRepository;
import com.example.genealogy.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Service
@Validated
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final Validator validator;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, Validator validator) {
        this.addressRepository = addressRepository;
        this.validator = validator;
    }

    public boolean existsById(@NotNull Long id) {
        return addressRepository.existsById(id);
    }

    @Override
    public boolean addressExists(@NotNull Address address) {
        return addressRepository.addressExists(address.getCountry(), address.getVoivodeship(), address.getCommunity(), address.getCity(), address.getAddress(), address.getPostalCode(), address.getParish(), address.getLongitude(), address.getLatitude(), address.getSecular());
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono adresu o id: " + id));
    }

    @Override
    public boolean saveAddress(Address address) {
        if (addressExists(address)) {
            return false;
        }

        validateAddress(address);

        try {
            addressRepository.save(address);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateAddress(@NotNull Address address) {
        if (!existsById(address.getId())) {
            return false;
        }

        validateAddress(address);

        try {
            addressRepository.save(address);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAddress(Address address) {
        try {
            if (existsById(address.getId())) {
                addressRepository.deleteById(address.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public List<Address> findByCountry(String country) {
        return addressRepository.findByCountry(country);
    }

    @Override
    public List<Address> findByVoivodeship(String voivodeship) {
        return addressRepository.findByVoivodeship(voivodeship);
    }

    @Override
    public List<Address> findByCommunity(String community) {
        return addressRepository.findByCommunity(community);
    }

    @Override
    public List<Address> findByCity(String city) {
        return addressRepository.findByCity(city);
    }

    @Override
    public List<Address> findByAddress(String address) {
        return addressRepository.findByAddress(address);
    }

    @Override
    public List<Address> findByPostalCode(String postalCode) {
        return addressRepository.findByPostalCode(postalCode);
    }

    @Override
    public List<Address> findByParish(String parish) {
        return addressRepository.findByParish(parish);
    }

    @Override
    public List<Address> findByCoordinates(Long longitude, Long latitude) {
        return addressRepository.findByCoordinates(longitude, latitude);
    }

    @Override
    public List<Address> findByLongitudeBetween(Long minLongitude, Long maxLongitude) {
        return addressRepository.findByLongitudeBetween(minLongitude, maxLongitude);
    }

    @Override
    public List<Address> findByLatitudeBetween(Long minLatitude, Long maxLatitude) {
        return addressRepository.findByLatitudeBetween(minLatitude, maxLatitude);
    }

    @Override
    public List<Address> findByPostalCodeBetween(String startPostalCode, String endPostalCode) {
        return addressRepository.findByPostalCodeBetween(startPostalCode, endPostalCode);
    }

    @Override
    public List<Address> searchAddress(String country, String voivodeship, String community, String city, String address, String postalCode, String startPostalCode, String endPostalCode, String parish,
                                       Long longitude, Long latitude, Long minLongitude, Long maxLongitude, Long minLatitude, Long maxLatitude) {

        List<Address> addresses = getAllAddresses();

        // Searching by country
        if (country != null && !country.isEmpty()) {
            List<Address> countryFilteredAddresses = findByCountry(country);
            addresses.retainAll(countryFilteredAddresses);
        }

        // Searching by voivodeship
        if (voivodeship != null && !voivodeship.isEmpty()) {
            List<Address> voivodeshipFilteredAddresses = findByVoivodeship(voivodeship);
            addresses.retainAll(voivodeshipFilteredAddresses);
        }

        // Searching by community
        if (community != null && !community.isEmpty()) {
            List<Address> communityFilteredAddresses = findByCommunity(community);
            addresses.retainAll(communityFilteredAddresses);
        }

        // Searching by city
        if (city != null && !city.isEmpty()) {
            List<Address> cityFilteredAddresses = findByCity(city);
            addresses.retainAll(cityFilteredAddresses);
        }

        // Searching by address
        if (address != null && !address.isEmpty()) {
            List<Address> addressFilteredAddresses = findByAddress(address);
            addresses.retainAll(addressFilteredAddresses);
        }

        // Searching by postal code
        if (postalCode != null && !postalCode.isEmpty()) {
            List<Address> postalCodeFilteredAddresses = findByPostalCode(postalCode);
            addresses.retainAll(postalCodeFilteredAddresses);
        }

        // Searching by postal code between
        if (startPostalCode != null && endPostalCode != null) {
            List<Address> postalCodeBetweenFilteredAddresses = findByPostalCodeBetween(startPostalCode, endPostalCode);
            addresses.retainAll(postalCodeBetweenFilteredAddresses);
        }

        // Searching by parish
        if (parish != null && !parish.isEmpty()) {
            List<Address> parishFilteredAddresses = findByParish(parish);
            addresses.retainAll(parishFilteredAddresses);
        }

        // Searching by coordinates
        if (longitude != null && latitude != null) {
            List<Address> coordinateFilteredAddresses = findByCoordinates(longitude, latitude);
            addresses.retainAll(coordinateFilteredAddresses);
        }

        // Searching by longitude range
        if (minLongitude != null && maxLongitude != null) {
            List<Address> longitudeFilteredAddresses = findByLongitudeBetween(minLongitude, maxLongitude);
            addresses.retainAll(longitudeFilteredAddresses);
        }

        // Searching by latitude range
        if (minLatitude != null && maxLatitude != null) {
            List<Address> latitudeFilteredAddresses = findByLatitudeBetween(minLatitude, maxLatitude);
            addresses.retainAll(latitudeFilteredAddresses);
        }

        return addresses;
    }

    private void validateAddress(Address address) {
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Address> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja adresu nie powiodła się:\n" + sb, violations);
        }
    }
}
