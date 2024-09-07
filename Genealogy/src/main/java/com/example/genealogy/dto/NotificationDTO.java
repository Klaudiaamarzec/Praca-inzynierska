package com.example.genealogy.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class NotificationDTO {

    private long id;
    private String title;
    private String context;
    private LocalDate date;
    private UserDTO user;
    private DocumentDTO document;
    private DocumentDTO newDocument;
}
