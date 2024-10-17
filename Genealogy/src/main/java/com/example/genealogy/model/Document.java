package com.example.genealogy.model;

import com.example.genealogy.validator.OnCreate;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="Document")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "confirmed")
    private Boolean confirmed = false;

    @Column(name="title", columnDefinition = "text")
    @NotNull(message = "Pole 'Tytuł' nie może być puste")
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
    @NotNull(message = "Adres nie może być pusty przy tworzeniu nowego dokumentu", groups = OnCreate.class)
    private Address place;

    @ManyToOne
    @JoinColumn(name = "ownerid")
    @NotNull(message = "Właściciel' nie może być puste", groups = OnCreate.class)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "type")
    @NotNull(message = "Pole 'Typ' nie może być puste", groups = OnCreate.class)
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
    @JsonIgnore
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "newDocument")
    @JsonIgnore
    private Set<Notification> editNotifications;

    @OneToMany(mappedBy = "document") // Relation to `PersonInDocument`
    private Set<PersonDocument> peopleDocuments;

    @JsonProperty("place")
    public Long getPlaceId() {
        return this.place != null ? this.place.getId() : null;
    }

    @JsonProperty("owner")
    public Long getOwnerId() {
        return this.owner != null ? this.owner.getId() : null;
    }

    @JsonProperty("type")
    public Integer getTypeId() {
        return this.type != null ? this.type.getId() : null;
    }

    @JsonProperty("localization")
    public Long getLocalizationId() {
        return this.localization != null ? this.localization.getId() : null;
    }

    @JsonProperty("photoRefers")
    public Long getPhotoRefersId() {
        return this.photoRefers != null ? this.photoRefers.getId() : null;
    }

    @JsonProperty("photos")
    public Set<Long> getPhotoIds() {
        return this.photos != null ? this.photos.stream().map(Document::getId).collect(Collectors.toSet()) : null;
    }

    @JsonProperty("peopleDocuments")
    public Set<Long> getPeopleDocumentIds() {
        return this.peopleDocuments != null
                ? this.peopleDocuments.stream()
                .map(PersonDocument::getId)
                .collect(Collectors.toSet())
                : null;
    }

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
