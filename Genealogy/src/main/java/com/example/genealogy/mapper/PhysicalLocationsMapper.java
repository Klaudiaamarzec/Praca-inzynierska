package com.example.genealogy.mapper;

import com.example.genealogy.dto.PhysicalLocationsDTO;
import com.example.genealogy.model.PhysicalLocations;
import org.springframework.stereotype.Component;

@Component
public class PhysicalLocationsMapper {

    private final LocationMapper locationMapper;
    private final LocalAddressMapper localAddressMapper;
    private final UserMapper userMapper;

    // Constructor-based injection
    public PhysicalLocationsMapper(LocationMapper locationMapper, LocalAddressMapper localAddressMapper, UserMapper userMapper) {
        this.locationMapper = locationMapper;
        this.localAddressMapper = localAddressMapper;
        this.userMapper = userMapper;
    }

    // Mapping from PhysicalLocations to PhysicalLocationsDTO
    public PhysicalLocationsDTO mapToDTO(PhysicalLocations physicalLocations) {
        if (physicalLocations == null) {
            return null;
        }

        PhysicalLocationsDTO dto = new PhysicalLocationsDTO();
        dto.setId(physicalLocations.getId());
        dto.setDate(physicalLocations.getDate());
        dto.setOriginal(physicalLocations.isOriginal());
        dto.setCondition(physicalLocations.getCondition());
        dto.setType(physicalLocations.getType());
        dto.setDescription(physicalLocations.getDescription());
        dto.setPhysical(locationMapper.mapToDTO(physicalLocations.getPhysical()));
        dto.setLocaladdress(localAddressMapper.mapToDTO(physicalLocations.getLocaladdress()));
        dto.setUser(userMapper.mapToDTO(physicalLocations.getUser()));

        return dto;
    }

    // Mapping from PhysicalLocationsDTO to PhysicalLocations
    public PhysicalLocations mapToEntity(PhysicalLocationsDTO dto) {
        if (dto == null) {
            return null;
        }

        PhysicalLocations physicalLocations = new PhysicalLocations();
        physicalLocations.setId(dto.getId());
        physicalLocations.setDate(dto.getDate());
        physicalLocations.setOriginal(dto.isOriginal());
        physicalLocations.setCondition(dto.getCondition());
        physicalLocations.setType(dto.getType());
        physicalLocations.setDescription(dto.getDescription());
        physicalLocations.setPhysical(locationMapper.mapToEntity(dto.getPhysical()));
        physicalLocations.setLocaladdress(localAddressMapper.mapToEntity(dto.getLocaladdress()));
        physicalLocations.setUser(userMapper.mapToEntity(dto.getUser()));

        return physicalLocations;
    }
}
