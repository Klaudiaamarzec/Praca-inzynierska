package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "personindocument")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PersonDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personid")
    @NotNull(message = "Osoba nie może być pusta")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "document")
    @NotNull(message = "Dokument nie może być pusty")
    private Document document;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    private Double x;

    private Double y;
}
