package com.example.genealogy.mapper;

import com.example.genealogy.dto.UserDTO;
import com.example.genealogy.model.User;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;
    private final DocumentMapper documentMapper;
    private final NotificationMapper notificationMapper;
    private final PhysicalLocationsMapper physicalLocationsMapper;

    // Constructor-based injection
    public UserMapper(
            RoleMapper roleMapper,
            DocumentMapper documentMapper,
            NotificationMapper notificationMapper,
            PhysicalLocationsMapper physicalLocationsMapper
    ) {
        this.roleMapper = roleMapper;
        this.documentMapper = documentMapper;
        this.notificationMapper = notificationMapper;
        this.physicalLocationsMapper = physicalLocationsMapper;
    }

    // Mapping from User to UserDTO
    public UserDTO mapToDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setIdRole(roleMapper.mapToDTO(user.getIdRole()));
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setMail(user.getMail());

        // Convert sets of documents, notifications, and physicalLocations
        if (user.getDocuments() != null) {
            dto.setDocuments(user.getDocuments().stream()
                    .map(documentMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        if (user.getNotifications() != null) {
            dto.setNotifications(user.getNotifications().stream()
                    .map(notificationMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        if (user.getPhysicalLocations() != null) {
            dto.setPhysicalLocations(user.getPhysicalLocations().stream()
                    .map(physicalLocationsMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    // Mapping from UserDTO to User
    public User mapToEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setIdRole(roleMapper.mapToEntity(dto.getIdRole()));
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setMail(dto.getMail());

        // Convert sets of documents, notifications, and physicalLocations
        if (dto.getDocuments() != null) {
            user.setDocuments(dto.getDocuments().stream()
                    .map(documentMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        if (dto.getNotifications() != null) {
            user.setNotifications(dto.getNotifications().stream()
                    .map(notificationMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        if (dto.getPhysicalLocations() != null) {
            user.setPhysicalLocations(dto.getPhysicalLocations().stream()
                    .map(physicalLocationsMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        return user;
    }
}
