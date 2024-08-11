package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "User") // Upewnienie się, że nazwa tabeli odpowiada nazwie w bazie danych
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Oznacza ze wartosci beda generowane automatycznie przez baze danych
    private long id;

    @ManyToOne
    @JoinColumn(name="idrole")
    private Role idRole;

    @Column(name="username")
    private String userName;

    @Column(name="password")
    private String password;

    @Column(name="mail", columnDefinition = "TEXT")
    private String mail;

    @OneToMany(mappedBy = "ownerID")
    private Set<Document> documents;

    @OneToMany(mappedBy = "userID")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "userID")
    private Set<PhysicalLocations> physicalLocations;
}
