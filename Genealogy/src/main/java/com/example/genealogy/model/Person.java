package com.example.genealogy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Imię nie może być puste")
    @Size(max = 256, message = "Imię może mieć maksymalnie 256 znaków")
    private String name;

    @Column(name = "surname", length = 256)
    @NotNull(message = "Nazwisko nie może być puste")
    @Size(max = 256, message = "Nazwisko może mieć maksymalnie 256 znaków")
    private String surname;

    @Column(name = "rin")
    private Long rin;

    @Column(name = "birthdate", columnDefinition = "Date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person")
    private Set<PersonDocument> personDocuments;

    @OneToOne(mappedBy = "child")
    private Family childFamily;

    @OneToMany(mappedBy = "father")
    private Set<Family> fatherFamilies;

    @OneToMany(mappedBy = "mother")
    private Set<Family> motherFamilies;

}
