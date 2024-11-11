package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", columnDefinition = "text")
    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;

    @Column(name = "context", columnDefinition = "text")
    @NotBlank(message = "Treść nie może być pusta")
    private String context;

    @Column(name = "displayed")
    @NotNull(message = "Pole 'Wyświetlone/Niewyświetlone' nie może być puste")
    private Boolean displayed;

    @Column(name = "date")
    @NotNull(message = "Data nie może być pusta")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userid")
    @NotNull(message = "Pole 'Użytkownik' nie może być puste")
    private User user;

    @ManyToOne
    @JoinColumn(name = "document")
    @NotNull(message = "Pole 'Dokument' nie może być puste")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "newdocument")
    private Document newDocument;

    @JsonProperty("user")
    public Map<String, Object> getUserDetails() {
        Map<String, Object> userDetails = new HashMap<>();
        if (user != null) {
            userDetails.put("userId", user.getId());
            userDetails.put("username", user.getUserName());
        }
        return userDetails.isEmpty() ? null : userDetails;
    }

    @JsonProperty("document")
    public Long getDocumentId() {
        return document != null ? document.getId() : null;
    }

    @JsonProperty("newDocument")
    public Long getNewDocumentId() {
        return newDocument != null ? newDocument.getId() : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Notification other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
