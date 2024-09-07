package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Set;

@Data
public class LocationDTO {

    private long id;
    private long physical;

    @NotNull(message = "Pole 'URL' nie może być puste")
    private long url;
    private DocumentDTO document;
    private Set<URLsDTO> urls;
    private Set<PhysicalLocationsDTO> physicalLocations;
}
