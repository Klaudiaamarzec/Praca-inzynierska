package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="idrole")
    @NotNull(message = "Rola ID nie może być pusta")
    private Role idRole;

    @Column(name="username", length = 64)
    @Size(max = 64, message = "Nazwa użytkownika nie może mieć więcej niż 64 znaki")
    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    private String userName;

    @Column(name="password", length = 256)
    @Size(max = 256, message = "Hasło nie może mieć więcej niż 256 znaków")
    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    @Column(name="mail", columnDefinition = "text")
    @NotNull(message = "Email nie może być pusty")
    private String mail;

    @Column(name="resettoken", columnDefinition = "text")
    private String resetToken;

    @Column(name = "tokenexpiration")
    private LocalDateTime tokenExpirationTime;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference("document-user")
    private Set<Document> documents;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private Set<PhysicalLocations> physicalLocations;
}
