package com.example.genealogy.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title", columnDefinition = "text")
    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;

    @Column(name = "context", columnDefinition = "text")
    @NotBlank(message = "Treść nie może być pusta")
    private String context;

    @Column(name = "displayed")
    @NotNull(message = "Pole 'Wyświetlone/Niewyświetlone' nie może być pusta")
    private boolean displayed;

    @Column(name = "date")
    @NotNull(message = "Data nie może być pusta")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "document")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "newdocument")
    private Document newDocument;
}
