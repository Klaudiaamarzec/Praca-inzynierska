package com.example.genealogy.service;

import com.example.genealogy.model.Role;

import java.util.List;

public interface RoleService {

    boolean saveRole(Role role);

    boolean updateRole(Role role);

    boolean existsById(Role role);

    boolean roleExist(Role role);

    boolean deleteRole(Role role);

    List<Role> getAllRoles();

    Role findByRoleName(String roleName);
}
