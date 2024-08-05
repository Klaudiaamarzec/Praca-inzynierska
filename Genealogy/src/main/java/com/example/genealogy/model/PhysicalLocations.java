package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "physicallocations")
@Data
public class PhysicalLocations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "Date", columnDefinition = "Date")
    private LocalDate date;

    @Column(name = "isoriginal")
    private boolean isOriginal;

    @Column(name = "Condition")
    private String condition;

    @Column(name = "type", columnDefinition = "TEXT")
    private String type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idphys") // Kolumna w tabeli URLS, która jest kluczem obcym do tabeli Location
    private Location physicalID;

    @ManyToOne
    @JoinColumn(name = "localaddressid")
    private LocalAddress localaddressID;

    @ManyToOne
    @JoinColumn(name = "User")
    private User userID;
}
