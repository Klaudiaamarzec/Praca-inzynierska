package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "person")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 256)
    @NotBlank(message = "Imię nie może być puste")
    @Size(max = 256, message = "Imię może mieć maksymalnie 256 znaków")
    private String name;

    @Column(name = "surname", length = 256)
    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(max = 256, message = "Nazwisko może mieć maksymalnie 256 znaków")
    private String surname;

    @Column(name = "rin")
    private Long rin;

    @Column(name = "birthdate", columnDefinition = "Date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<PersonDocument> personDocuments;

    @OneToOne(mappedBy = "child")
    private Family childFamily;

    @OneToMany(mappedBy = "father")
    private Set<Family> fatherFamilies;

    @OneToMany(mappedBy = "mother")
    private Set<Family> motherFamilies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return id != null && id.equals(person.id);
    }

    // Niestandardowy getter, zwraca tylko ID dla personDocuments
    @JsonProperty("personDocuments")
    public Set<Map<String, Object>> getPersonDocumentIds() {
        return personDocuments != null ? personDocuments.stream()
                .map(personDocument -> {
                    Map<String, Object> documentData = new HashMap<>();
                    documentData.put("personDocumentId", personDocument.getId());
                    documentData.put("documentId", personDocument.getDocument().getId());
                    documentData.put("documentTitle", personDocument.getDocument().getTitle());
                    return documentData;
                })
                .collect(Collectors.toSet()) : null;
    }

    // Niestandardowy getter dla childFamily, zwraca ID
    @JsonProperty("childFamily")
    public Long getChildFamilyId() {
        return childFamily != null ? childFamily.getId() : null;
    }

    // Niestandardowy getter dla fatherFamilies, zwraca tylko ID
    @JsonProperty("fatherFamilies")
    public Set<Long> getFatherFamilyIds() {
        return fatherFamilies != null ? fatherFamilies.stream()
                .map(Family::getId)
                .collect(Collectors.toSet()) : null;
    }

    // Niestandardowy getter dla motherFamilies, zwraca tylko ID
    @JsonProperty("motherFamilies")
    public Set<Long> getMotherFamilyIds() {
        return motherFamilies != null ? motherFamilies.stream()
                .map(Family::getId)
                .collect(Collectors.toSet()) : null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
