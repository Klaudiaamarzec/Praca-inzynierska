package com.example.genealogy.model;

import com.example.genealogy.validator.OnCreate;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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

    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "additional_fields", joinColumns = @JoinColumn(name = "document_id"))
    @MapKeyColumn(name = "field_name")
    @Column(name = "field_value")
    private Map<String, String> additionalFields = new HashMap<>();

    @OneToOne
    @JoinColumn(name = "localization")
    private Location localization;

    @ManyToOne
    @JoinColumn(name = "photorefers")
    private Document photoRefers;

    @Column(name="path")
    private String path;

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

    @JsonProperty("owner")
    public Long getOwnerId() {
        return this.owner != null ? this.owner.getId() : null;
    }

    @JsonProperty("localization")
    public Location getLocalizationData() {
        return this.localization != null ? this.localization : null;
    }

    @JsonProperty("type")
    public Map<String, Object> getTypeId() {
        if (this.type != null) {
            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("id", this.type.getId());
            typeMap.put("name", this.type.getTypeName());
            return typeMap;
        }
        return null;
    }

    @JsonProperty("photoRefers")
    public Long getPhotoRefersId() {
        return this.photoRefers != null ? this.photoRefers.getId() : null;
    }

    @JsonProperty("photos")
    public Set<Map<String, Object>> getPhotosWithPaths() {
        return this.photos != null
                ? this.photos.stream()
                .map(photo -> {
                    Map<String, Object> photoDetails = new HashMap<>();
                    photoDetails.put("id", photo.getId());
                    photoDetails.put("path", photo.getPath());
                    photoDetails.put("title", photo.getTitle());
                    return photoDetails;
                })
                .collect(Collectors.toSet())
                : null;
    }

    @JsonProperty("peopleDocuments")
    public Set<Map<String, Object>> getPeopleDocumentDetails() {
        return this.peopleDocuments != null
                ? this.peopleDocuments.stream()
                .map(personDocument -> {
                    Map<String, Object> personDetails = new HashMap<>();
                    personDetails.put("id", personDocument.getPerson().getId());
                    personDetails.put("firstName", personDocument.getPerson() != null ? personDocument.getPerson().getName() : null);
                    personDetails.put("lastName", personDocument.getPerson() != null ? personDocument.getPerson().getSurname() : null);
                    personDetails.put("comment", personDocument.getComment());
                    personDetails.put("x", personDocument.getX());
                    personDetails.put("y", personDocument.getY());
                    return personDetails;
                })
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
