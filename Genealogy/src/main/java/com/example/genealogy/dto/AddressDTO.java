package com.example.genealogy.dto;

import lombok.Data;

@Data
public class AddressDTO {

    private long id;
    private String country;
    private String voivodeship;
    private String community;
    private String city;
    private String address;
    private String postalCode;
    private String parish;
    private String secular;
    private Long longitude;
    private Long latitude;
}