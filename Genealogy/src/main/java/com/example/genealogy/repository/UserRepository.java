package com.example.genealogy.repository;

import com.example.genealogy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    // Find all users with genealogist role
    @Query("SELECT u FROM User u WHERE u.idRole.id = 1")
    List<User> findGenealogist();

    // Find all users
    @Query("SELECT u FROM User u WHERE u.idRole.id = 2")
    List<User> findUsers();

}
