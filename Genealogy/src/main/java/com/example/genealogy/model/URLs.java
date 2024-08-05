package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "urls")
@Data
public class URLs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "idurl") // Kolumna w tabeli URLS, która jest kluczem obcym do tabeli Location
    private Location urlID;

    private String url;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}
