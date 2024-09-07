package com.example.genealogy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class NotificationDTO {

    private long id;

    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;

    @NotBlank(message = "Treść nie może być pusta")
    private String context;

    @NotNull(message = "Data nie może być pusta")
    private LocalDate date;

    @NotNull(message = "Pole 'Wyświetlone/Niewyświetlone' nie może być pusta")
    private boolean displayed;

    private UserDTO user;
    private DocumentDTO document;
    private DocumentDTO newDocument;
}
