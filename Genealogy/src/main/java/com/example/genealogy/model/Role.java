package com.example.genealogy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name="Role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="rolename", length = 64)
    @Size(max = 64, message = "Nazwa roli nie może mieć więcej niż 64 znaki")
    @NotNull(message = "Nazwa roli nie może być pusta")
    private String roleName;

    @OneToMany(mappedBy = "idRole")
    private Set<User> users;
}
