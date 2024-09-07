package com.example.genealogy.mapper;

import com.example.genealogy.dto.UserDTO;
import com.example.genealogy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private PhysicalLocationsMapper physicalLocationsMapper;

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
                    .map(document -> new DocumentMapper().mapToDTO(document))
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
                    .map(documentDTO -> new DocumentMapper().mapToEntity(documentDTO))
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
