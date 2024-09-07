package com.example.genealogy.dto;
import lombok.Data;

@Data
public class FamilyDTO {

    private long id;
    private PersonDTO child;
    private PersonDTO father;
    private PersonDTO mother;
}
