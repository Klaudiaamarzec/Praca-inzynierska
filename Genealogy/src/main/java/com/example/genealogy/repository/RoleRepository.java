package com.example.genealogy.repository;

import com.example.genealogy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

    @Query("SELECT r from Role r WHERE r.roleName = :role")
    Role findByRoleName(@Param("role") String role);

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Role r
    WHERE (lower(r.rolename) = lower(:roleName))
""", nativeQuery = true)
    boolean existsRole(@Param("roleName") String roleName);

}
