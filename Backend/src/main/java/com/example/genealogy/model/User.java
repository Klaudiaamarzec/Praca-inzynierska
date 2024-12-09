package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JsonIgnore
    private Set<Document> documents;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<PhysicalLocations> physicalLocations;

    @JsonProperty("idRole")
    public Integer getRoleId() {
        return idRole != null ? idRole.getId() : null;
    }

    @JsonProperty("documents")
    public Set<Long> getDocumentIds() {
        return documents != null ? documents.stream().map(Document::getId).collect(Collectors.toSet()) : null;
    }
}
