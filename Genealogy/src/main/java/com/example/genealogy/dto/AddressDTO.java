package com.example.genealogy.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDTO {

    private long id;

    @Size(max = 128, message = "Nazwa kraju nie może mieć więcej niż 128 znaków")
    private String country;

    @Size(max = 128, message = "Nazwa województwa nie może mieć więcej niż 128 znaków")
    private String voivodeship;

    @Size(max = 128, message = "Nazwa gminy nie może mieć więcej niż 128 znaków")
    private String community;

    @Size(max = 128, message = "Nazwa miasta nie może mieć więcej niż 128 znaków")
    private String city;

    private String address;

    @Size(max = 15, message = "Kod pocztowy nie może mieć więcej niż 15 znaków")
    private String postalCode;

    private String parish;

    private String secular;

    private Long longitude;

    private Long latitude;
}