package com.example.genealogy.mapper;

import com.example.genealogy.dto.LocalAddressDTO;
import com.example.genealogy.model.LocalAddress;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class LocalAddressMapper {

    private final PhysicalLocationsMapper physicalLocationsMapper;

    // Constructor-based injection
    public LocalAddressMapper(PhysicalLocationsMapper physicalLocationsMapper) {
        this.physicalLocationsMapper = physicalLocationsMapper;
    }

    // Mapping from LocalAddress to LocalAddressDTO
    public LocalAddressDTO mapToDTO(LocalAddress localAddress) {
        if (localAddress == null) {
            return null;
        }

        LocalAddressDTO dto = new LocalAddressDTO();
        dto.setId(localAddress.getId());
        dto.setCountry(localAddress.getCountry());
        dto.setVoivodeship(localAddress.getVoivodeship());
        dto.setCommunity(localAddress.getCommunity());
        dto.setCity(localAddress.getCity());
        dto.setAddress(localAddress.getAddress());
        dto.setPostalcode(localAddress.getPostalcode());

        if (localAddress.getPhysicalLocations() != null) {
            dto.setPhysicalLocations(localAddress.getPhysicalLocations().stream()
                    .map(physicalLocationsMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    // Mapping from LocalAddressDTO to LocalAddress
    public LocalAddress mapToEntity(LocalAddressDTO dto) {
        if (dto == null) {
            return null;
        }

        LocalAddress localAddress = new LocalAddress();
        localAddress.setId(dto.getId());
        localAddress.setCountry(dto.getCountry());
        localAddress.setVoivodeship(dto.getVoivodeship());
        localAddress.setCommunity(dto.getCommunity());
        localAddress.setCity(dto.getCity());
        localAddress.setAddress(dto.getAddress());
        localAddress.setPostalcode(dto.getPostalcode());

        if (dto.getPhysicalLocations() != null) {
            localAddress.setPhysicalLocations(dto.getPhysicalLocations().stream()
                    .map(physicalLocationsMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        return localAddress;
    }
}
