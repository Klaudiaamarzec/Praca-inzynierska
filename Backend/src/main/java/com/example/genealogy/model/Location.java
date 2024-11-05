package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "location")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idphys")
    private Long physical;

    @Column(name = "idurl")
    private Long url;

    @OneToOne(mappedBy = "localization")
    private Document document;

    @OneToMany(mappedBy = "urlID", fetch = FetchType.EAGER)
    private Set<URLs> urls;

    @OneToMany(mappedBy = "physical", fetch = FetchType.EAGER)
    private Set<PhysicalLocations> physicalLocations;

    @Override
    public int hashCode() {
        return Objects.hash(id); // Użyj tylko unikalnych identyfikatorów
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id);
    }

}
