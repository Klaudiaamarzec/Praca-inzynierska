package com.example.genealogy.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.Set;

@Data
public class DocumentTypeDTO {

    private int id;
    private String typeName;
    private String template;
    private Set<DocumentDTO> documents;
    private JsonNode parsedTemplate;
}
