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

    @OneToOne(mappedBy = "location")
    private Document document;

    @OneToMany(mappedBy = "location") // Odwrotna strona relacji jeden do jednego
    private Set<URLs> urls;

    @OneToMany(mappedBy = "location") // Odwrotna strona relacji jeden do jednego
    private Set<PhysicalLocations> physicalLocations;
}
