package com.example.genealogy.mapper;

import com.example.genealogy.dto.RoleDTO;
import com.example.genealogy.model.Role;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final UserMapper userMapper;

    // Constructor-based injection
    public RoleMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // Mapping from Role to RoleDTO
    public RoleDTO mapToDTO(Role role) {
        if (role == null) {
            return null;
        }

        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());

        // Convert set of users
        if (role.getUsers() != null) {
            dto.setUsers(role.getUsers().stream()
                    .map(userMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    // Mapping from RoleDTO to Role
    public Role mapToEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        role.setId(dto.getId());
        role.setRoleName(dto.getRoleName());

        // Convert set of users
        if (dto.getUsers() != null) {
            role.setUsers(dto.getUsers().stream()
                    .map(userMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        return role;
    }
}
