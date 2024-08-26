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

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "surname", length = 256)
    private String surname;

    @Column(name = "rin")
    private Long rin;

    @Column(name = "birthdate", columnDefinition = "Date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person") // Relacja do `PersonDocument`
    private Set<PersonDocument> personDocuments;

    @OneToOne(mappedBy = "child") // Relacja do `Family`
    private Family childFamily;

    @OneToMany(mappedBy = "father")
    private Set<Family> fatherFamilies;

    @OneToMany(mappedBy = "mother")
    private Set<Family> motherFamilies;

}
