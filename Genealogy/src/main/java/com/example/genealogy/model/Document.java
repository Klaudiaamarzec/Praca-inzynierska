package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="Document")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "confirmed")
    private boolean confirmed;

    @Column(name="title", columnDefinition = "text")
    private String title;

    @Column(name="startdate", columnDefinition = "Date")
    private LocalDate startDate;

    @Column(name="enddate", columnDefinition = "Date")
    private LocalDate endDate;

    @Column(name="description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name= "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "place")
    private Address place;

    @ManyToOne
    @JoinColumn(name = "ownerid")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "type")
    private DocumentType type;

    @OneToOne
    @JoinColumn(name = "localization")
    private Location localization;

    @ManyToOne
    @JoinColumn(name = "photography")
    private Document photography;

    // Relacja odwrotna

    @OneToMany(mappedBy = "photography")
    private Set<Document> photos;

    @OneToMany(mappedBy = "document")
    private Set<Notification> notifications;

//    @OneToMany(mappedBy = "newDocumentID")
//    private Set<Notification> editNotifications;

    @OneToMany(mappedBy = "document") // Relacja do `PersonInDocument`
    private Set<PersonDocument> peopleDocuments;
}
