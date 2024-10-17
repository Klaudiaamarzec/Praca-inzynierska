package com.example.genealogy.repository;

import com.example.genealogy.model.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void checkIfExist() {

        boolean result = notificationRepository.existsNotification("Dodano nowy dokument", "Dokument 4", false, LocalDate.of(2003,10,6),3L, 4L, null);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testFindNotificationByUser() {

        Long userID = 2L;
        List<Notification> notifications = notificationRepository.findNotificationByUser(userID);

        assertThat(notifications).hasSize(2);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
        assertThat(notifications.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindNotificationByUser2() {

        Long userID = 5L;
        List<Notification> notifications = notificationRepository.findNotificationByUser(userID);
        assertThat(notifications).hasSize(0);
    }

    @Test
    void testFindNotificationsNotDisplayed() {

        List<Notification> notifications = notificationRepository.findNotificationsNotDisplayed();
        assertThat(notifications).hasSize(3);
    }

    @Test
    void testFindNotificationByTitle() {

        List<Notification> notifications = notificationRepository.findNotificationByTitle("dodano");

        assertThat(notifications).hasSize(3);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
        assertThat(notifications.get(1).getId()).isEqualTo(2L);
        assertThat(notifications.get(2).getId()).isEqualTo(1L);
    }

    @Test
    void testNotFindNotificationByTitle() {

        List<Notification> notifications = notificationRepository.findNotificationByTitle("dokumenty");
        assertThat(notifications).hasSize(0);
    }

    @Test
    void testFindNotificationByTitleContext() {

        List<Notification> notifications = notificationRepository.findNotificationByTitleContext("dodano", "context");

        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
    }

    @Test
    void testFindNotificationByParameter() {

        List<Notification> notifications = notificationRepository.findNotificationByParameter("dodano");

        assertThat(notifications).hasSize(3);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
        assertThat(notifications.get(1).getId()).isEqualTo(2L);
        assertThat(notifications.get(2).getId()).isEqualTo(1L);
    }

    @Test
    void testFindNotificationByParameter2() {

        List<Notification> notifications = notificationRepository.findNotificationByParameter("uzytkownik");

        assertThat(notifications).hasSize(1);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testFindNotificationsAboutEdit() {

        List<Notification> notifications = notificationRepository.findNotificationsAboutEdit();
        assertThat(notifications).hasSize(0);
    }

    @Test
    void testFindNotificationsAboutAdd() {

        List<Notification> notifications = notificationRepository.findNotificationsAboutAdd();

        assertThat(notifications).hasSize(3);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
        assertThat(notifications.get(1).getId()).isEqualTo(2L);
        assertThat(notifications.get(2).getId()).isEqualTo(1L);
    }
}
