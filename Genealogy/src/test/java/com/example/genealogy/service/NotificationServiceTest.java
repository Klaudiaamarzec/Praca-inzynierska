package com.example.genealogy.service;

import com.example.genealogy.model.Notification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Test
    void testExistsById() {
        boolean result = notificationService.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testNotificationExists() {
        Notification notification = new Notification();
        notification.setTitle("Dodano nowy dokument");
        notification.setContext("Dokument 4");
        notification.setDisplayed(false);
        notification.setDate(LocalDate.of(2003,10,6));
        notification.setUser(userService.getUserById(3L));
        notification.setDocument(documentService.getDocumentById(4L));

        boolean result = notificationService.notificationExists(notification);
        assertThat(result).isTrue();
    }

    @Test
    void testNotificationNotExists() {
        Notification notification = new Notification();
        notification.setTitle("Dodano nowy dokument");
        notification.setContext("Dokument");
        notification.setDisplayed(true);

        boolean result = notificationService.notificationExists(notification);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveNotification() {

        Notification notification = new Notification();
        notification.setTitle("Zaktualizowano nowy dokument");
        notification.setContext("Fotografia 18");
        notification.setDisplayed(false);
        notification.setDate(LocalDate.of(2015,9,12));
        notification.setUser(userService.getUserById(3L));
        notification.setDocument(documentService.getDocumentById(4L));

        boolean isSaved = notificationService.saveNotification(notification);
        assertThat(isSaved).isTrue();
        assertThat(notificationService.getAllNotifications()).contains(notification);
    }

    @Test
    void testSaveNotNotification() {

        Notification notification = new Notification();
        notification.setTitle("Zaktualizowano nowy dokument");
        notification.setContext("Fotografia 18");
        notification.setDisplayed(false);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> notificationService.saveNotification(notification));

        // Sprawdzenie czy wyjątek zawiera odpowiednią wiadomość walidacyjną
        assertThat(thrown.getConstraintViolations()).hasSize(1); // Weryfikacja liczby błędów walidacji

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Data nie może być pusta");
    }

    @Test
    void testNotSaveNotification() {

        Notification notification = new Notification();
        notification.setTitle("Zaktualizowano nowy dokument");
        notification.setContext("Fotografia 18");

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> notificationService.saveNotification(notification));

        assertThat(thrown.getConstraintViolations()).hasSize(4);

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Pole 'Wyświetlone/Niewyświetlone' nie może być puste");
        assertThat(messages).contains("Data nie może być pusta");
        assertThat(messages).contains("Pole 'Użytkownik' nie może być puste");
        assertThat(messages).contains("Pole 'Dokument' nie może być puste");
    }

    @Test
    void testGetNotificationById() {

        Notification notification = notificationService.getNotificationById(2L);
        assertThat(notification).isNotNull();
        assertThat(notification.getContext()).isEqualTo("Uzytkownik");
    }

    @Test
    void testNotGetNotificationById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> notificationService.getNotificationById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono powiadomienia o id: " + 99);
    }

    @Test
    void testUpdateNotification() {
        Notification notification = notificationService.getNotificationById(1L);
        notification.setTitle("Nowy tytuł");

        boolean isUpdated = notificationService.updateNotification(notification);
        assertThat(isUpdated).isTrue();

        Notification updatedNotification = notificationService.getNotificationById(notification.getId());
        assertThat(updatedNotification.getTitle()).isEqualTo("Nowy tytuł");
    }

    @Test
    void testDeleteNotification() {
        Notification notificationToDelete = new Notification();
        notificationToDelete.setId(1L);

        boolean result = notificationService.deleteNotification(notificationToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> notificationService.getNotificationById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono powiadomienia o id: " + notificationToDelete.getId());
    }

    @Test
    void testNotDeleteNotification() {
        Notification nonExistentNotification = new Notification();
        nonExistentNotification.setId(99L);

        boolean result = notificationService.deleteNotification(nonExistentNotification);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        assertThat(notifications).hasSize(3);
    }

    @Test
    void testFindNotificationByUser() {

        List<Notification> notifications = notificationService.findNotificationByUser(userService.getUserById(2L));

        assertThat(notifications).hasSize(2);

        assertThat(notifications.get(0).getId()).isEqualTo(3L);
        assertThat(notifications.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindNotificationsNotDisplayed() {

        List<Notification> notifications = notificationService.findNotificationsNotDisplayed();

        assertThat(notifications).hasSize(3);

        assertThat(notifications)
                .extracting(Notification::getId)
                .containsExactly(3L, 2L, 1L);
    }

    @Test
    void testFindNotifications() {

        List<Notification> notifications = notificationService.findNotifications("dokument");

        assertThat(notifications).hasSize(3);

        assertThat(notifications)
                .extracting(Notification::getId)
                .containsExactly(1L, 3L, 2L);
    }

    @Test
    void testFindNotifications2() {

        List<Notification> notifications = notificationService.findNotifications("dokument 4");

        assertThat(notifications).hasSize(3);
    }

    @Test
    void testFindNotifications3() {

        List<Notification> notifications = notificationService.findNotifications("uzytkownik");

        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testFindNotificationsAboutEdit() {

        List<Notification> notifications = notificationService.findNotificationsAboutEdit();

        assertThat(notifications).hasSize(1);
    }

    @Test
    void testFindNotificationsAboutAdd() {

        List<Notification> notifications = notificationService.findNotificationsAboutAdd();

        assertThat(notifications).hasSize(3);

        assertThat(notifications)
                .extracting(Notification::getId)
                .containsExactly(3L, 2L, 1L);
    }
}
