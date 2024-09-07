package com.example.genealogy.mapper;

import com.example.genealogy.dto.*;
import com.example.genealogy.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class DocumentMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DocumentTypeMapper documentTypeMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private DateMapper dateMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private PersonDocumentMapper personDocumentMapper;

    // Mapping from Document to DocumentDTO
    public DocumentDTO mapToDTO(Document document) {
        if (document == null) {
            return null;
        }

        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setConfirmed(document.isConfirmed());
        dto.setTitle(document.getTitle());
        dto.setStartDate(document.getStartDate());
        dto.setEndDate(document.getEndDate());
        dto.setDescription(document.getDescription());

        dto.setDate(dateMapper.mapToDTO(document.getDate()));
        dto.setPlace(addressMapper.mapToDTO(document.getPlace()));
        dto.setOwner(userMapper.mapToDTO(document.getOwner()));
        dto.setType(documentTypeMapper.mapToDTO(document.getType()));
        dto.setLocalization(locationMapper.mapToDTO(document.getLocalization()));
        dto.setPhotoRefers(mapToDTO(document.getPhotoRefers()));

        // Convert sets to DTOs using mappers
        dto.setPhotos(document.getPhotos().stream().map(this::mapToDTO).collect(Collectors.toSet()));
        dto.setNotification(document.getNotifications().stream().map(notificationMapper::mapToDTO).collect(Collectors.toSet()));
        dto.setEditNotification(document.getEditNotifications().stream().map(notificationMapper::mapToDTO).collect(Collectors.toSet()));
        dto.setPeopleDocument(document.getPeopleDocuments().stream().map(personDocumentMapper::mapToDTO).collect(Collectors.toSet()));

        return dto;
    }

    // Mapping from DocumentDTO to Document
    public Document mapToEntity(DocumentDTO dto) {
        if (dto == null) {
            return null;
        }

        Document document = new Document();
        document.setId(dto.getId());
        document.setConfirmed(dto.isConfirmed());
        document.setTitle(dto.getTitle());
        document.setStartDate(dto.getStartDate());
        document.setEndDate(dto.getEndDate());
        document.setDescription(dto.getDescription());

        document.setDate(dateMapper.mapToEntity(dto.getDate()));
        document.setPlace(addressMapper.mapToEntity(dto.getPlace()));
        document.setOwner(userMapper.mapToEntity(dto.getOwner()));
        document.setType(documentTypeMapper.mapToEntity(dto.getType()));
        document.setLocalization(locationMapper.mapToEntity(dto.getLocalization()));
        document.setPhotoRefers(mapToEntity(dto.getPhotoRefers()));

        // Convert sets to entities using mappers
        document.setPhotos(dto.getPhotos().stream().map(this::mapToEntity).collect(Collectors.toSet()));
        document.setNotifications(dto.getNotification().stream().map(notificationMapper::mapToEntity).collect(Collectors.toSet()));
        document.setEditNotifications(dto.getEditNotification().stream().map(notificationMapper::mapToEntity).collect(Collectors.toSet()));
        document.setPeopleDocuments(dto.getPeopleDocument().stream().map(personDocumentMapper::mapToEntity).collect(Collectors.toSet()));

        return document;
    }
}
