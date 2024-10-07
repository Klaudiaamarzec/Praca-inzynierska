package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.User;
import com.example.genealogy.repository.UserRepository;
import com.example.genealogy.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;

    private final JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validator validator, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.mailSender = mailSender;
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
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public boolean saveUser(@NotNull User user) {
        if (userExists(user)) {
            return false;
        }

        validateUser(user);

        try {
            //user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    public String createPasswordResetToken(User user) {
        // Generowanie unikalnego tokenu
        String token = UUID.randomUUID().toString();

        // Przechowywanie tokenu w bazie danych powiązanego z użytkownikiem, aby go później weryfikować
        user.setResetToken(token);
        user.setTokenExpirationTime(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        return token;
    }

    @Override
    public void sendResetPasswordEmail(String email, String token) {

        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token; // Adres URL do resetowania hasła
        String subject = "Resetowanie hasła";
        String body = "Kliknij poniższy link, aby zresetować swoje hasło:\n" + resetLink;

        // Przygotowanie wiadomości e-mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("genealogy.application@onet.pl");

        // Wysyłanie e-maila
        mailSender.send(message);
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
            throw new ConstraintViolationException("Walidacja użytkownika nie powiodła się:\n" + sb, violations);
        }
    }
}
