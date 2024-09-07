package com.example.genealogy.mapper;

import com.example.genealogy.dto.LocationDTO;
import com.example.genealogy.model.Location;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class LocationMapper {

    private final DocumentMapper documentMapper;
    private final URLsMapper urlsMapper;
    private final PhysicalLocationsMapper physicalLocationsMapper;

    // Constructor-based injection
    public LocationMapper(DocumentMapper documentMapper, URLsMapper urlsMapper, PhysicalLocationsMapper physicalLocationsMapper) {
        this.documentMapper = documentMapper;
        this.urlsMapper = urlsMapper;
        this.physicalLocationsMapper = physicalLocationsMapper;
    }

    // Mapping from Location to LocationDTO
    public LocationDTO mapToDTO(Location location) {
        if (location == null) {
            return null;
        }

        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setPhysical(location.getPhysical());
        dto.setUrl(location.getUrl());
        dto.setDocument(documentMapper.mapToDTO(location.getDocument()));

        if (location.getUrls() != null) {
            dto.setUrls(location.getUrls().stream()
                    .map(urlsMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        if (location.getPhysicalLocations() != null) {
            dto.setPhysicalLocations(location.getPhysicalLocations().stream()
                    .map(physicalLocationsMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    // Mapping from LocationDTO to Location
    public Location mapToEntity(LocationDTO dto) {
        if (dto == null) {
            return null;
        }

        Location location = new Location();
        location.setId(dto.getId());
        location.setPhysical(dto.getPhysical());
        location.setUrl(dto.getUrl());
        location.setDocument(documentMapper.mapToEntity(dto.getDocument()));

        if (dto.getUrls() != null) {
            location.setUrls(dto.getUrls().stream()
                    .map(urlsMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        if (dto.getPhysicalLocations() != null) {
            location.setPhysicalLocations(dto.getPhysicalLocations().stream()
                    .map(physicalLocationsMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        return location;
    }
}
