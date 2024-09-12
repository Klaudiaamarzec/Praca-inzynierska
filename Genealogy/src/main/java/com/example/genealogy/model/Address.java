package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    private Long longitude;

    private Long latitude;

    @OneToMany(mappedBy = "place")
    private Set<Document> documents;
}
