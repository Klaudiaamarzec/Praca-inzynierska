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

    private String country;

    private String voivodeship;

    private String community;

    private String city;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "postalcode")
    private int postalCode;

    private String parish;

    private String secular;

    private long longitude;

    private long latitude;

    @OneToMany(mappedBy = "place")
    private Set<Document> documents;
}
