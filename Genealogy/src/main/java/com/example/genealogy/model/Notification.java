package com.example.genealogy.model;

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
    private String title;

    @Column(name = "context", columnDefinition = "text")
    private String context;

    @Column(name = "Date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "User")
    private User user;

    @ManyToOne
    @JoinColumn(name = "document")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "newdocument")
    private Document newDocument;
}
