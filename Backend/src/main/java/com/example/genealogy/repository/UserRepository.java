package com.example.genealogy.repository;

import com.example.genealogy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Find user by email
    @Query("SELECT u FROM User u WHERE u.mail = :email")
    User findByEmail(@Param("email") String email);

    // Find user by username
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    User findByUserName(@Param("username") String username);

    // Find user by token
    @Query("SELECT u FROM User u WHERE u.resetToken = :resetToken")
    User findByResetToken(@Param("resetToken") String resetToken);

    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Users u
        WHERE (u.userName = :userName)
        OR (u.mail = :mail)
        AND (u.idRole = :roleId)
    """, nativeQuery = true)
    boolean existsUser(@Param("roleId") Integer roleId,
                       @Param("userName") String userName,
                       @Param("mail") String mail);
}
