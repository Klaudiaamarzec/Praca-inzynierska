package com.example.genealogy.repository;

import com.example.genealogy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    @Query("SELECT r from Role r WHERE r.roleName = :role")
    Role findByRoleName(@Param("role") String role);
}
