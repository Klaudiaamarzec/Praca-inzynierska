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

    @Column(name = "country", length = 128)
    private String country;

    @Column(name = "voivodeship", length = 128)
    private String voivodeship;

    @Column(name = "community", length = 128)
    private String community;

    @Column(name = "city", length = 128)
    private String city;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "postalcode", length = 15)
    private String postalcode;

    @OneToMany(mappedBy = "localaddress")
    private Set<PhysicalLocations> physicalLocations;
}
