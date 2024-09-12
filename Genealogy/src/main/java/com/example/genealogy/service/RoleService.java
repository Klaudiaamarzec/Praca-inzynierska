package com.example.genealogy.service;

import com.example.genealogy.model.Role;

import java.util.List;

public interface RoleService {

    boolean saveRole(Role role);

    boolean updateRole(Role role);

    boolean existsById(long id);

    boolean deleteRole(Role role);

    List<Role> getAllRoles();

    Role findByRoleName(String roleName);
}
