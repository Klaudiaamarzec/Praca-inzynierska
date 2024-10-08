package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "location")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idphys")
    private Long physical;

    @Column(name = "idurl")
    @NotNull(message = "Pole 'URL' nie może być puste")
    private Long url;

    @OneToOne(mappedBy = "localization")
    private Document document;

    @OneToMany(mappedBy = "url")
    private Set<URLs> urls;

    @OneToMany(mappedBy = "physical")
    private Set<PhysicalLocations> physicalLocations;
}
