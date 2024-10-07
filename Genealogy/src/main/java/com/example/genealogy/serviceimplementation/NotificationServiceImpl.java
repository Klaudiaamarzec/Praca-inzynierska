package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Notification;
import com.example.genealogy.model.User;
import com.example.genealogy.repository.NotificationRepository;
import com.example.genealogy.service.NotificationService;
import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final Validator validator;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, Validator validator) {
        this.notificationRepository = notificationRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Long id) {
        return notificationRepository.existsById(id);
    }

    @Override
    public boolean notificationExists(@NotNull Notification notification) {
        return notificationRepository.existsNotification(
                notification.getTitle(),
                notification.getContext(),
                notification.getDisplayed(),
                notification.getDate(),
                notification.getUser() != null ? notification.getUser().getId() : null,
                notification.getDocument() != null ? notification.getDocument().getId() : null,
                notification.getNewDocument() != null ? notification.getNewDocument().getId() : null);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono powiadomienia o id: " + id));
    }

    @Override
    public boolean saveNotification(@NotNull Notification notification) {
        if (notificationExists(notification)) {
            return false;
        }

        validateNotification(notification);

        try {
            notificationRepository.save(notification);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateNotification(@NotNull Notification notification) {
        if (!existsById(notification.getId())) {
            return false;
        }

        validateNotification(notification);

        try {
            notificationRepository.save(notification);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteNotification(Notification notification) {
        try {
            if (existsById(notification.getId())) {
                notificationRepository.delete(notification);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Observed
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> findNotificationByDocumentList(List<Long> documentIDs) {
        return notificationRepository.findNotificationByDocumentList(documentIDs);
    }

    @Override
    public List<Notification> findNotificationByUser(@NotNull User user) {
        return notificationRepository.findNotificationByUser(user.getId());
    }

    @Override
    public List<Notification> findNotificationsNotDisplayed() {
        return notificationRepository.findNotificationsNotDisplayed();
    }

    @Override
    public List<Notification> findNotifications(@NotNull String parameter) {

        String[] keywords = parameter.split("\\s+");
        List<Notification> notifications = new ArrayList<>();

        for (String keyword : keywords) {
            notifications.addAll(notificationRepository.findNotificationByTitleContext(keyword, keyword));
        }

        for (String keyword : keywords) {
            notifications.addAll(notificationRepository.findNotificationByTitle(keyword));
        }

        for (String keyword : keywords) {
            notifications.addAll(notificationRepository.findNotificationByParameter(keyword));
        }

        return notifications.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Notification> findNotificationsAboutEdit() {
        return notificationRepository.findNotificationsAboutEdit();
    }

    @Override
    public List<Notification> findNotificationsAboutAdd() {
        return notificationRepository.findNotificationsAboutAdd();
    }

    private void validateNotification(Notification notification) {
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Notification> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja powiadomienia nie powiodła się:\n" + sb, violations);
        }
    }
}
