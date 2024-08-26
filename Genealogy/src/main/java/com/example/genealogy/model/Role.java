package com.example.genealogy.model;

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
    private String roleName;

    @OneToMany(mappedBy = "idRole")
    private Set<User> users;
}
