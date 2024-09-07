package com.example.genealogy.serviceimplementation;

import com.example.genealogy.dto.AddressDTO;
import com.example.genealogy.mapper.AddressMapper;
import com.example.genealogy.model.Address;
import com.example.genealogy.repository.AddressRepository;
import com.example.genealogy.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public void saveAddress(AddressDTO addressDTO) {
        Address address = addressMapper.mapToEntity(addressDTO);
        addressRepository.save(address);
    }

    @Override
    public boolean deleteAddress(AddressDTO addressDTO) {
        if (addressRepository.existsById(addressDTO.getId())) {
            addressRepository.deleteById(addressDTO.getId());
            return true;
        }
        return false;
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
    public boolean addressExists(AddressDTO addressDTO) {
        return addressRepository.addressExists(addressDTO.getCountry(), addressDTO.getVoivodeship(), addressDTO.getCity(), addressDTO.getAddress());
    }

    public boolean exists(AddressDTO addressDTO) {
        Address address = addressMapper.mapToEntity(addressDTO);
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
}
