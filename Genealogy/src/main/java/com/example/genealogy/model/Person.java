package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "person")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String surname;

    @OneToOne
    @JoinColumn(name = "mid")
    private Person mid;

    @OneToOne
    @JoinColumn(name = "fid")
    private Person fid;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Person cid;

    // Relacja odwrotna

    @OneToMany(mappedBy = "cid")
    private Set<Person> kids;

    private long rid;

    @Column(name = "birthdate", columnDefinition = "Date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person") // Relacja do `PersonDocument`
    private Set<PersonDocument> personDocuments;
}
