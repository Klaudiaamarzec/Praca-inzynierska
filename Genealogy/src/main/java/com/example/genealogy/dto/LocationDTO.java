package com.example.genealogy.dto;

import lombok.Data;
import java.util.Set;

@Data
public class LocationDTO {

    private long id;
    private long physical;
    private long url;
    private DocumentDTO document;
    private Set<URLsDTO> urls;
    private Set<PhysicalLocationsDTO> physicalLocations;
}
