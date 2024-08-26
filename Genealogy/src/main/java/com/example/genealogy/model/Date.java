package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Date")
@Data
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dateid")
    private long id;

    private Integer year;

    private Integer month;

    private Integer day;

    @OneToMany(mappedBy = "date") // Odwrotna strona relacji ManyToOne
    private Set<Document> documents;
}
