package com.example.genealogy.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PhysicalLocationsDTO {

    private long id;
    private LocalDate date;
    private boolean isOriginal;
    private String condition;
    private String type;
    private String description;
    private LocationDTO physical;
    private LocalAddressDTO localaddress;
    private UserDTO user;
}
