package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.URLs;
import com.example.genealogy.model.User;
import com.example.genealogy.repository.UserRepository;
import com.example.genealogy.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean userExists(@NotNull User user) {
        return userRepository.existsUser(user.getIdRole() != null ? user.getIdRole().getId() : null, user.getUserName(), user.getMail());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o id: " + id));
    }

    @Override
    public boolean saveUser(@NotNull User user) {
        if (userExists(user)) {
            return false;
        }

        validateUser(user);

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateUser(@NotNull User user) {
        if (!existsById(user.getId())) {
            return false;
        }

        validateUser(user);

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            if (existsById(user.getId())) {
                userRepository.delete(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getGenealogists() {
        return userRepository.findGenealogist();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findUsers();
    }

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja użytkownika nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
