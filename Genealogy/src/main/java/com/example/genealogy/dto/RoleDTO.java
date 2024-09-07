package com.example.genealogy.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {

    private int id;
    private String roleName;
    private Set<UserDTO> users;
}
