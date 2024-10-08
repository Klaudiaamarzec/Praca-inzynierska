package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "physicallocations")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PhysicalLocations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", columnDefinition = "Date")
    @NotNull(message = "Data nie może być pusta")
    private LocalDate date;

    @Column(name = "isoriginal")
    @NotNull(message = "Pole 'Oryginalne/Nieoryginalne' nie może być puste")
    private Boolean isOriginal;

    @Column(name = "condition", length = 64)
    @Size(max = 64, message = "Kondycja nie może mieć więcej niż 64 znaki")
    private String condition;

    @Column(name = "type", columnDefinition = "text")
    private String type;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idphys") // Column in URLs table that is a foreign key to Location table
    @NotNull(message = "Pole 'Lokalizacja fizyczna' nie może być puste")
    private Location physical;

    @ManyToOne
    @JoinColumn(name = "localaddressid")
    @NotNull(message = "Pole 'Adres lokalny' nie może być puste")
    private LocalAddress localaddress;

    @ManyToOne
    @JoinColumn(name = "userid")
    @NotNull(message = "Pole 'Użytkownik' nie może być puste")
    private User user;
}
