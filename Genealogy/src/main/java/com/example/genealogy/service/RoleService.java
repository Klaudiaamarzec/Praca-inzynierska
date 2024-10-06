package com.example.genealogy.service;

import com.example.genealogy.model.PhysicalLocations;
import com.example.genealogy.model.Role;

import java.util.List;

public interface RoleService {

    boolean existsById(Integer id);

    boolean roleExists(Role role);

    Role getRoleById(Integer id);

    boolean saveRole(Role role);

    boolean updateRole(Role role);

    boolean deleteRole(Role role);

    List<Role> getAllRoles();

    Role findByRoleName(String roleName);
}
