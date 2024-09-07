package com.example.genealogy.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class LocalAddressDTO {

    private long id;

    @Size(max = 128, message = "Nazwa kraju może mieć maksymalnie 128 znaków")
    private String country;

    @Size(max = 128, message = "Nazwa województwa może mieć maksymalnie 128 znaków")
    private String voivodeship;

    @Size(max = 128, message = "Nazwa gminy może mieć maksymalnie 128 znaków")
    private String community;

    @Size(max = 128, message = "Nazwa miasta może mieć maksymalnie 128 znaków")
    private String city;

    private String address;

    @Size(max = 15, message = "Kod pocztowy nie może mieć więcej niż 15 znaków")
    private String postalcode;
    private Set<PhysicalLocationsDTO> physicalLocations;
}
