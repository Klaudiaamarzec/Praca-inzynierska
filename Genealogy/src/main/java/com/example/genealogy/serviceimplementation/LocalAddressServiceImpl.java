package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.LocalAddress;
import com.example.genealogy.repository.LocalAddressRepository;
import com.example.genealogy.service.LocalAddressService;
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
    public boolean saveLocalAddress(@NotNull LocalAddress localAddress) {
        if (addressExists(localAddress.getCountry(), localAddress.getVoivodeship(), localAddress.getCity(), localAddress.getAddress())) {
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
    public boolean existsById(long id) {
        return localAddressRepository.existsById(id);
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
    public List<LocalAddress> findByCountryAndCity(String country, String city) {
        return localAddressRepository.findByCountryAndCity(country, city);
    }

    @Override
    public boolean addressExists(String country, String voivodeship, String city, String address) {
        return localAddressRepository.addressExists(country, voivodeship, city, address);
    }

    @Override
    public List<LocalAddress> getAddress(String country, String voivodeship, String city, String address) {
        return localAddressRepository.getAddress(country, voivodeship, city, address);
    }

    private void validateLocalAddress(LocalAddress localAddress) {
        Set<ConstraintViolation<LocalAddress>> violations = validator.validate(localAddress);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<LocalAddress> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja adresu lokalnego nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
