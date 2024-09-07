package com.example.genealogy.dto;

import com.example.genealogy.model.Notification;
import com.example.genealogy.model.PersonDocument;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class DocumentDTO {

    private long id;
    private boolean confirmed;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    // Relations to other entities
    private DateDTO date;
    private AddressDTO place;
    private UserDTO owner;
    private DocumentTypeDTO type;
    private LocationDTO localization;
    private DocumentDTO photoRefers;

    private Set<DocumentDTO> photos;
    private Set<NotificationDTO> notification;
    private Set<NotificationDTO> editNotification;
    private Set<PersonDocumentDTO> peopleDocument;
}

