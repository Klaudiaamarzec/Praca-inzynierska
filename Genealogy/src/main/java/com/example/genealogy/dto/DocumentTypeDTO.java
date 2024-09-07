package com.example.genealogy.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class DocumentTypeDTO {

    private int id;

    @NotBlank(message = "Nazwa typu nie może być pusta")
    @Size(max = 64, message = "Nazwa typu nie może być dłuższa niż 64 znaki")
    private String typeName;

    @NotBlank(message = "Szablon nie może być pusty")
    private String template;
    private Set<DocumentDTO> documents;
    private JsonNode parsedTemplate;
}
