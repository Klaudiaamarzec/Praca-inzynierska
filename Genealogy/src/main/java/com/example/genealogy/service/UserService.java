package com.example.genealogy.service;

import com.example.genealogy.model.URLs;
import com.example.genealogy.model.User;

import java.util.List;

public interface UserService {

    boolean existsById(Long id);

    boolean userExists(User user);

    User getUserById(Long id);

    boolean saveUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);

    List<User> getAllUsers();

    List<User> getGenealogists();

    List<User> getUsers();
}
