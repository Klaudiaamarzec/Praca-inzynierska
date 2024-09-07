package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {

    private int id;

    @Size(max = 64, message = "Nazwa roli nie może mieć więcej niż 64 znaki")
    @NotNull(message = "Nazwa roli nie może być pusta")
    private String roleName;

    private Set<UserDTO> users;
}
