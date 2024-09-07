package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonDocumentDTO {

    private long id;

    @NotNull(message = "Osoba nie może być pusta")
    private PersonDTO person;

    @NotNull(message = "Dokument nie może być pusty")
    private DocumentDTO document;
    private String comment;
    private Integer x;
    private Integer y;
}
