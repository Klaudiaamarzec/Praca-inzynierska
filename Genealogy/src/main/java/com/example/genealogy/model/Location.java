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
    private long physicalID;

    @Column(name = "idurl")
    private long urlID;

    @OneToOne(mappedBy = "localization")
    private Document document;

    @OneToMany(mappedBy = "urlID") // Odwrotna strona relacji jeden do jednego
    private Set<URLs> urls;

    @OneToMany(mappedBy = "physicalID") // Odwrotna strona relacji jeden do jednego
    private Set<PhysicalLocations> physicalLocations;
}
