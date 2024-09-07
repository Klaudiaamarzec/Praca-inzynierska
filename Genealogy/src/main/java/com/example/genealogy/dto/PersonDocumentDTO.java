package com.example.genealogy.dto;

import lombok.Data;

@Data
public class PersonDocumentDTO {

    private long id;
    private PersonDTO person;
    private DocumentDTO document;
    private String comment;
    private Integer x;
    private Integer y;
}
