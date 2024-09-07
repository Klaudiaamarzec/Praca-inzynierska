package com.example.genealogy.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FamilyDTO {

    private long id;

    @NotNull(message = "Pole 'Dziecko' nie może być puste")
    private PersonDTO child;

    @NotNull(message = "Pole 'Ojciec' nie może być puste")
    private PersonDTO father;

    @NotNull(message = "Pole 'Matka' nie może być puste")
    private PersonDTO mother;
}
