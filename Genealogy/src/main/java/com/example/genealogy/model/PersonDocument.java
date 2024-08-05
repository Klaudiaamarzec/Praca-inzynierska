package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "personindocument")
@Data
public class PersonDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "personid")
    private Person personID;

    @ManyToOne
    @JoinColumn(name = "document")
    private Document documentID;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    private int x;

    private int y;
}
