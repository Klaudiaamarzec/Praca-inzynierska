package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "localaddress")
@Data
public class LocalAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String country;

    private String voivodeship;

    private String community;

    private String city;

    private String address;

    private String postalcode;

    @OneToMany(mappedBy = "localaddress")
    private Set<PhysicalLocations> physicalLocations;
}
