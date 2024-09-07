package com.example.genealogy.mapper;

import com.example.genealogy.dto.AddressDTO;
import com.example.genealogy.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    // Helper method to map Address to AddressDTO
    public AddressDTO mapToDTO(Address address) {

        if (address == null) {
            return null;
        }

        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setCountry(address.getCountry());
        dto.setVoivodeship(address.getVoivodeship());
        dto.setCommunity(address.getCommunity());
        dto.setCity(address.getCity());
        dto.setAddress(address.getAddress());
        dto.setPostalCode(address.getPostalCode());
        dto.setParish(address.getParish());
        dto.setSecular(address.getSecular());
        dto.setLongitude(address.getLongitude());
        dto.setLatitude(address.getLatitude());
        return dto;
    }

    // Helper method to map AddressDTO to Address
    public Address mapToEntity(AddressDTO dto) {

        if(dto == null) {
            return null;
        }
        Address address = new Address();
        address.setId(dto.getId());
        address.setCountry(dto.getCountry());
        address.setVoivodeship(dto.getVoivodeship());
        address.setCommunity(dto.getCommunity());
        address.setCity(dto.getCity());
        address.setAddress(dto.getAddress());
        address.setPostalCode(dto.getPostalCode());
        address.setParish(dto.getParish());
        address.setSecular(dto.getSecular());
        address.setLongitude(dto.getLongitude());
        address.setLatitude(dto.getLatitude());
        return address;
    }
}
