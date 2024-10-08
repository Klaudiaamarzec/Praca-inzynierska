package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="Document")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "confirmed")
    @NotNull(message = "Potwierdzenie nie może być puste")
    private Boolean confirmed;

    @Column(name="title", columnDefinition = "text")
    private String title;

    @Column(name="startdate", columnDefinition = "Date")
    @PastOrPresent(message = "Data rozpoczęcia musi być w przeszłości lub dzisiaj")
    private LocalDate startDate;

    @Column(name="enddate", columnDefinition = "Date")
    @PastOrPresent(message = "Data zakończenia musi być w przeszłości lub dzisiaj")
    private LocalDate endDate;

    @Column(name="description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name= "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "place")
    @NotNull(message = "Adres nie może być pusty")
    private Address place;

    @ManyToOne
    @JoinColumn(name = "ownerid")
    @NotNull(message = "Pole 'Właściciel' nie może być puste")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "type")
    @NotNull(message = "Typ nie może być pusty")
    private DocumentType type;

    @OneToOne
    @JoinColumn(name = "localization")
    private Location localization;

    @ManyToOne
    @JoinColumn(name = "photorefers")
    private Document photoRefers;

    // Opposite relation

    @OneToMany(mappedBy = "photoRefers")
    private Set<Document> photos;

    @OneToMany(mappedBy = "document")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "newDocument")
    private Set<Notification> editNotifications;

    @OneToMany(mappedBy = "document") // Relation to `PersonInDocument`
    private Set<PersonDocument> peopleDocuments;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Document other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
