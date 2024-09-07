package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PhysicalLocationsDTO {

    private long id;

    @NotNull(message = "Data nie może być pusta")
    private LocalDate date;

    @NotNull(message = "Pole 'Oryginalne/Nieoryginalne' nie może być puste")
    private boolean isOriginal;

    @Size(max = 64, message = "Kondycja nie może mieć więcej niż 64 znaki")
    private String condition;

    private String type;

    private String description;

    @NotNull(message = "Pole 'Lokalizacja fizyczna' nie może być puste")
    private LocationDTO physical;

    @NotNull(message = "Pole 'Adres lokalny' nie może być puste")
    private LocalAddressDTO localaddress;

    @NotNull(message = "Pole 'Użytkownik' nie może być puste")
    private UserDTO user;
}
