package com.example.genealogy.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class PersonDTO {

    private long id;
    private String name;
    private String surname;
    private Long rin;
    private LocalDate birthDate;
    private Set<PersonDocumentDTO> personDocuments;
    private FamilyDTO childFamily;
    private Set<FamilyDTO> fatherFamilies;
    private Set<FamilyDTO> motherFamilies;
}
