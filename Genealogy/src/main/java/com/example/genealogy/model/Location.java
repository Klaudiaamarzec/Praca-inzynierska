package com.example.genealogy.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "location")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "idphys")
    private long physical;

    @Column(name = "idurl")
    @NotNull(message = "Pole 'URL' nie może być puste")
    private long url;

    @OneToOne(mappedBy = "localization")
    private Document document;

    @OneToMany(mappedBy = "url")
    private Set<URLs> urls;

    @OneToMany(mappedBy = "physical")
    private Set<PhysicalLocations> physicalLocations;
}
