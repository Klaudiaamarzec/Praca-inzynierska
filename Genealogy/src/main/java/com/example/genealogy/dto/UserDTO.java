package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private long id;

    @NotNull(message = "Rola ID nie może być pusta")
    private RoleDTO idRole;

    @Size(max = 64, message = "Nazwa użytkownika nie może mieć więcej niż 64 znaki")
    @NotNull(message = "Nazwa użytkownika nie może być pusta")
    private String userName;

    @Size(max = 256, message = "Hasło nie może mieć więcej niż 256 znaków")
    @NotNull(message = "Hasło nie może być puste")
    private String password;

    @NotNull(message = "Email nie może być pusty")
    private String mail;

    private Set<DocumentDTO> documents;
    private Set<NotificationDTO> notifications;
    private Set<PhysicalLocationsDTO> physicalLocations;
}
