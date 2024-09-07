package com.example.genealogy.mapper;

import com.example.genealogy.dto.NotificationDTO;
import com.example.genealogy.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    private final UserMapper userMapper;
    private final DocumentMapper documentMapper;

    // Constructor-based injection
    public NotificationMapper(UserMapper userMapper, DocumentMapper documentMapper) {
        this.userMapper = userMapper;
        this.documentMapper = documentMapper;
    }

    // Mapping from Notification to NotificationDTO
    public NotificationDTO mapToDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setContext(notification.getContext());
        dto.setDate(notification.getDate());
        dto.setUser(userMapper.mapToDTO(notification.getUser()));
        dto.setDocument(documentMapper.mapToDTO(notification.getDocument()));
        dto.setNewDocument(documentMapper.mapToDTO(notification.getNewDocument()));

        return dto;
    }

    // Mapping from NotificationDTO to Notification
    public Notification mapToEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setTitle(dto.getTitle());
        notification.setContext(dto.getContext());
        notification.setDate(dto.getDate());
        notification.setUser(userMapper.mapToEntity(dto.getUser()));
        notification.setDocument(documentMapper.mapToEntity(dto.getDocument()));
        notification.setNewDocument(documentMapper.mapToEntity(dto.getNewDocument()));

        return notification;
    }
}
