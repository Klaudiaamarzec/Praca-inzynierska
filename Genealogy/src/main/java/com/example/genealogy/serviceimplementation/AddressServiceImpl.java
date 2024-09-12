package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Address;
import com.example.genealogy.repository.AddressRepository;
import com.example.genealogy.service.AddressService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
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
    public boolean existsById(long id) {
        return addressRepository.existsById(id);
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
    public List<Address> findByCityAndVoivodeship(String city, String voivodeship) {
        return addressRepository.findByCityAndVoivodeship(city, voivodeship);
    }

    @Override
    public List<Address> findByCountryAndPostalCode(String country, String postalCode) {
        return addressRepository.findByCountryAndPostalCode(country, postalCode);
    }

    @Override
    public List<Address> findByParishAndCity(String parish, String city) {
        return addressRepository.findByParishAndCity(parish, city);
    }

    @Override
    public List<Address> findByPostalCodeBetween(String startPostalCode, String endPostalCode) {
        return addressRepository.findByPostalCodeBetween(startPostalCode, endPostalCode);
    }

    @Override
    public boolean addressExists(@NotNull Address address) {
        return addressRepository.addressExists(address.getCountry(), address.getVoivodeship(), address.getCity(), address.getAddress());
    }

    public boolean exists(@NotNull Address address) {
        return addressRepository.existsById(address.getId());
    }

    @Override
    public List<Address> getAddress(String country, String voivodeship, String city, String address) {
        return addressRepository.getAddress(country, voivodeship, city, address);
    }

    @Override
    public List<Address> getAddressesByParam(String parameter) {
        return addressRepository.getAddressesByParam(parameter);
    }

    @Override
    public List<Address> getAddressesByAllParams(String country, String voivodeship, String community, String city, Long longitude, Long latitude, String address, String postalCode, String parish, String secular) {
        return addressRepository.getAddressesByAllParams(country, voivodeship, community, city, longitude, latitude, address, postalCode, parish, secular);
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
            throw new ConstraintViolationException("Walidacja adresu nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
