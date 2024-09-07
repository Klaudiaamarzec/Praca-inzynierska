package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class PersonDTO {

    private long id;

    @NotNull(message = "Imię nie może być puste")
    @Size(max = 256, message = "Imię może mieć maksymalnie 256 znaków")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @Size(max = 256, message = "Nazwisko może mieć maksymalnie 256 znaków")
    private String surname;

    private Long rin;
    private LocalDate birthDate;
    private Set<PersonDocumentDTO> personDocuments;
    private FamilyDTO childFamily;
    private Set<FamilyDTO> fatherFamilies;
    private Set<FamilyDTO> motherFamilies;
}
