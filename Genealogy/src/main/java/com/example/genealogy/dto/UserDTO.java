package com.example.genealogy.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private long id;
    private RoleDTO idRole;
    private String userName;
    private String password;
    private String mail;
    private Set<DocumentDTO> documents;
    private Set<NotificationDTO> notifications;
    private Set<PhysicalLocationsDTO> physicalLocations;
}
