package com.example.genealogy.model;

import com.example.genealogy.validator.OnCreate;
import com.fasterxml.jackson.annotation.*;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "date", columnDefinition = "Date")
    @NotNull(message = "Data nie może być pusta", groups = OnCreate.class)
    private LocalDate date;

    @Column(name = "isoriginal")
    @NotNull(message = "Pole 'Oryginalne/Nieoryginalne' nie może być puste", groups = OnCreate.class)
    private Boolean isOriginal;

    @Column(name = "condition", length = 64)
    @Size(max = 64, message = "Kondycja nie może mieć więcej niż 64 znaki")
    private String condition;

    @Column(name = "type", columnDefinition = "text")
    private String type;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idphys", nullable = false) // Column in URLs table that is a foreign key to Location table
    @NotNull(message = "Pole 'Lokalizacja fizyczna' nie może być puste", groups = OnCreate.class)
    @JsonBackReference
    private Location physical;

    @ManyToOne
    @JoinColumn(name = "localaddressid", nullable = false)
    @NotNull(message = "Pole 'Adres lokalny' nie może być puste", groups = OnCreate.class)
    private LocalAddress localaddress;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @NotNull(message = "Pole 'Użytkownik' nie może być puste", groups = OnCreate.class)
    private User user;

    @JsonProperty("physical")
    public Long getPhysicalId() {
        return this.physical != null ? this.physical.getId() : null;
    }

    @JsonProperty("user")
    public Long getUserId() {
        return this.user != null ? this.user.getId() : null;
    }
}
