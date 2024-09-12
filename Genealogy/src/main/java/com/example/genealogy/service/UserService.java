package com.example.genealogy.service;

import com.example.genealogy.model.User;

import java.util.List;

public interface UserService {

    boolean saveUser(User user);

    boolean updateUser(User user);

    boolean existsById(long id);

    boolean deleteUser(User user);

    List<User> findAllUsers();

    List<User> findGenealogists();

    List<User> findUsers();
}
