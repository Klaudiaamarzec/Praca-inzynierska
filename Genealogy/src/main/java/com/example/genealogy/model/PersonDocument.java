package com.example.genealogy.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "personindocument")
@Data
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

    private Integer x;

    private Integer y;
}
