package com.example.genealogy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class DocumentDTO {

    private long id;

    @NotNull(message = "Potwierdzenie nie może być puste")
    private boolean confirmed;
    private String title;

    @PastOrPresent(message = "Data rozpoczęcia musi być w przeszłości lub dzisiaj")
    private LocalDate startDate;

    @PastOrPresent(message = "Data zakończenia musi być w przeszłości lub dzisiaj")
    private LocalDate endDate;
    private String description;

    // Relations to other entities
    private DateDTO date;

    @NotNull(message = "Adres nie może być pusty")
    private AddressDTO place;

    @NotNull(message = "Pole 'Właściciel' nie może być puste")
    private UserDTO owner;

    @NotNull(message = "Typ nie może być pusty")
    private DocumentTypeDTO type;

    private LocationDTO localization;

    private DocumentDTO photoRefers;

    private Set<DocumentDTO> photos;

    private Set<NotificationDTO> notification;

    private Set<NotificationDTO> editNotification;

    private Set<PersonDocumentDTO> peopleDocument;
}

