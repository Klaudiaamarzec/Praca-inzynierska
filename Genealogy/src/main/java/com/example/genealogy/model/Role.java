package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name="Role")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="rolename", length = 64)
    @Size(max = 64, message = "Nazwa roli nie może mieć więcej niż 64 znaki")
    @NotBlank(message = "Nazwa roli nie może być pusta")
    private String roleName;

    @OneToMany(mappedBy = "idRole")
    private Set<User> users;
}
