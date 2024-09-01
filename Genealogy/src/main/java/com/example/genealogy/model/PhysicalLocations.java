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

    @Column(name = "date", columnDefinition = "Date")
    private LocalDate date;

    @Column(name = "isoriginal")
    private boolean isOriginal;

    @Column(name = "condition", length = 64)
    private String condition;

    @Column(name = "type", columnDefinition = "text")
    private String type;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idphys") // Column in URLs table that is a foreign key to Location table
    private Location physical;

    @ManyToOne
    @JoinColumn(name = "localaddressid")
    private LocalAddress localaddress;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
}
