package com.example.genealogy.dto;

import lombok.Data;

import java.util.Set;

@Data
public class LocalAddressDTO {

    private long id;
    private String country;
    private String voivodeship;
    private String community;
    private String city;
    private String address;
    private String postalcode;
    private Set<PhysicalLocationsDTO> physicalLocations;
}
