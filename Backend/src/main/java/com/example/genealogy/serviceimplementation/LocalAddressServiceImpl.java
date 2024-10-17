package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.LocalAddress;
import com.example.genealogy.repository.LocalAddressRepository;
import com.example.genealogy.service.LocalAddressService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LocalAddressServiceImpl implements LocalAddressService {

    private final LocalAddressRepository localAddressRepository;
    private final Validator validator;

    @Autowired
    public LocalAddressServiceImpl(LocalAddressRepository localAddressRepository, Validator validator) {
        this.localAddressRepository = localAddressRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Long id) {
        return localAddressRepository.existsById(id);
    }

    @Override
    public boolean localAddressExists(@NotNull LocalAddress localAddress) {
        return localAddressRepository.existsLocalAddress(localAddress.getCountry(), localAddress.getVoivodeship(), localAddress.getCommunity(), localAddress.getCity(), localAddress.getAddress(), localAddress.getPostalCode());
    }

    @Override
    public LocalAddress getLocalAddressById(Long id) {
        return localAddressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono lokalnego adresu o id: " + id));
    }

    @Override
    public LocalAddress getLocalAddressByAllParams(String country, String voivodeship, String community, String city, String address, String postalCode) {
        return localAddressRepository.getLocalAddressByAllParams(country, voivodeship, community, city, address, postalCode);
    }

    @Override
    public boolean saveLocalAddress(@NotNull LocalAddress localAddress) {
        if (localAddressExists(localAddress)) {
            return false;
        }

        validateLocalAddress(localAddress);

        try {
            localAddressRepository.save(localAddress);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateLocalAddress(@NotNull LocalAddress localAddress) {
        if (!existsById(localAddress.getId())) {
            return false;
        }

        validateLocalAddress(localAddress);

        try {
            localAddressRepository.save(localAddress);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<LocalAddress> getAllLocalAddresses() {
        return localAddressRepository.findAll();
    }

    @Override
    public boolean deleteLocalAddress(LocalAddress localAddress) {
        try {
            if (existsById(localAddress.getId())) {
                localAddressRepository.delete(localAddress);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<LocalAddress> findLocalAddressByCountry(String country) {
        return localAddressRepository.findLocalAddressByCountry(country);
    }

    @Override
    public List<LocalAddress> findLocalAddressByVoivodeship(String voivodeship) {
        return localAddressRepository.findLocalAddressByVoivodeship(voivodeship);
    }

    @Override
    public List<LocalAddress> findLocalAddressByCity(String city) {
        return localAddressRepository.findLocalAddressByCity(city);
    }

    @Override
    public List<LocalAddress> searchLocalAddress(String country, String voivodeship, String city) {

        List<LocalAddress> addresses = getAllLocalAddresses();

        // Searching by country
        if (country != null && !country.isEmpty()) {
            List<LocalAddress> countryFilteredAddresses = findLocalAddressByCountry(country);
            addresses.retainAll(countryFilteredAddresses);
        }

        // Searching by country
        if (voivodeship != null && !voivodeship.isEmpty()) {
            List<LocalAddress> voivodeshipFilteredAddresses = findLocalAddressByVoivodeship(voivodeship);
            addresses.retainAll(voivodeshipFilteredAddresses);
        }

        // Searching by country
        if (city != null && !city.isEmpty()) {
            List<LocalAddress> cityFilteredAddresses = findLocalAddressByCity(city);
            addresses.retainAll(cityFilteredAddresses);
        }

        return addresses;
    }

    private void validateLocalAddress(LocalAddress localAddress) throws ConstraintViolationException {
        Set<ConstraintViolation<LocalAddress>> violations = validator.validate(localAddress);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<LocalAddress> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja adresu lokalnego nie powiodła się:\n" + sb, violations);
        }
    }
}
