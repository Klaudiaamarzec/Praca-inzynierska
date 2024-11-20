package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "address")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid")
    private Long id;

    @Column(name = "country", length = 128)
    @Size(max = 128, message = "Nazwa kraju nie może mieć więcej niż 128 znaków")
    private String country;

    @Column(name = "voivodeship", length = 128)
    @Size(max = 128, message = "Nazwa województwa nie może mieć więcej niż 128 znaków")
    private String voivodeship;

    @Column(name = "community", length = 128)
    @Size(max = 128, message = "Nazwa gminy nie może mieć więcej niż 128 znaków")
    private String community;

    @Column(name = "city", length = 128)
    @Size(max = 128, message = "Nazwa miasta nie może mieć więcej niż 128 znaków")
    private String city;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "postalcode", length = 15)
    @Size(max = 15, message = "Kod pocztowy nie może mieć więcej niż 15 znaków")
    private String postalCode;

    @Column(name = "parish", columnDefinition = "TEXT")
    private String parish;

    @Column(name = "secular", columnDefinition = "TEXT")
    private String secular;

    private Double longitude;

    private Double latitude;

    @OneToMany(mappedBy = "place")
    @JsonIgnore
    private Set<Document> documents;
}
