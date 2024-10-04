package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Role;
import com.example.genealogy.repository.RoleRepository;
import com.example.genealogy.service.RoleService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final Validator validator;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, Validator validator) {
        this.roleRepository = roleRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Role role) {
        return roleRepository.existsById(role.getId());
    }

    @Override
    public boolean roleExist(@NotNull Role role) {
        return roleRepository.existsRole(role.getRoleName());
    }

    @Override
    public boolean saveRole(@NotNull Role role) {
        if (roleExist(role)) {
            return false;
        }

        validateRole(role);

        try {
            roleRepository.save(role);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateRole(@NotNull Role role) {
        if (!existsById(role)) {
            return false;
        }

        validateRole(role);

        try {
            roleRepository.save(role);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteRole(Role role) {
        try {
            if (existsById(role)) {
                roleRepository.delete(role);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    private void validateRole(Role role) {
        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Role> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja roli nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
