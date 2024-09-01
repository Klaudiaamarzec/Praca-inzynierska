package com.example.genealogy.model;

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
    private long url;

    @OneToOne(mappedBy = "localization")
    private Document document;

    @OneToMany(mappedBy = "url")
    private Set<URLs> urls;

    @OneToMany(mappedBy = "physical")
    private Set<PhysicalLocations> physicalLocations;
}
