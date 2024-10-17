package com.example.genealogy.service;

import com.example.genealogy.model.User;

import java.util.List;

public interface UserService {

    boolean existsById(Long id);

    boolean userExists(User user);

    User getUserById(Long id);

    User findByEmail(String email);

    User findByResetToken(String resetToken);

    User findByUserName(String userName);

    boolean saveUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);

    List<User> getAllUsers();

    List<User> getGenealogists();

    List<User> getUsers();

    String createPasswordResetToken(User user);
    void sendResetPasswordEmail(String email, String token);
}
