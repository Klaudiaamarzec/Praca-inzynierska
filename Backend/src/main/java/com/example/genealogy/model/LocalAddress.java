package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "localaddress")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LocalAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country", length = 128)
    @Size(max = 128, message = "Nazwa kraju może mieć maksymalnie 128 znaków")
    private String country;

    @Column(name = "voivodeship", length = 128)
    @Size(max = 128, message = "Nazwa województwa może mieć maksymalnie 128 znaków")
    private String voivodeship;

    @Column(name = "community", length = 128)
    @Size(max = 128, message = "Nazwa gminy może mieć maksymalnie 128 znaków")
    private String community;

    @Column(name = "city", length = 128)
    @Size(max = 128, message = "Nazwa miasta może mieć maksymalnie 128 znaków")
    private String city;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "postalcode", length = 15)
    @Size(max = 15, message = "Kod pocztowy nie może mieć więcej niż 15 znaków")
    private String postalCode;

    @OneToMany(mappedBy = "localaddress")
    private Set<PhysicalLocations> physicalLocations;
}
