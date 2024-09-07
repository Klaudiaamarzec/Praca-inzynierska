package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class URLsDTO {

    private long id;

    @NotNull(message = "URL id nie może być puste")
    private LocationDTO urlID;

    @NotNull(message = "URL nie może być pusta")
    private String url;

    @Size(max = 256, message = "Komentarz nie może mieć więcej niż 256 znaków")
    private String comment;
}
