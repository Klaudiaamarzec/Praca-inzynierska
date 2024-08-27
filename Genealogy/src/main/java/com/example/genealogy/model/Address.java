package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid")
    private long id;

    @Column(name = "country", length = 128)
    private String country;

    @Column(name = "voivodeship", length = 128)
    private String voivodeship;

    @Column(name = "community", length = 128)
    private String community;

    @Column(name = "city", length = 128)
    private String city;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "postalcode", length = 15)
    private String postalCode;

    @Column(name = "parish", columnDefinition = "TEXT")
    private String parish;

    @Column(name = "secular", columnDefinition = "TEXT")
    private String secular;

    private Long longitude;

    private Long latitude;

    @OneToMany(mappedBy = "place")
    private Set<Document> documents;
}
