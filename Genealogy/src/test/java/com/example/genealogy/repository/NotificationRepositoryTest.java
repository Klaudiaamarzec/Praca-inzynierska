package com.example.genealogy.repository;

import com.example.genealogy.model.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    void testFindNotificationByDocumentList() {

        List<Long> documentIDs = Arrays.asList(2L, 6L);
        List<Notification> notifications = notificationRepository.findNotificationByDocumentList(documentIDs);

        assertThat(notifications).hasSize(1);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
    }

    @Test
    void testFindNotificationByDocumentList2() {

        List<Long> documentIDs = Arrays.asList(2L, 6L, 4L, 5L, 3L);
        List<Notification> notifications = notificationRepository.findNotificationByDocumentList(documentIDs);

        assertThat(notifications).hasSize(3);

        // ID Check
        assertThat(notifications.get(0).getId()).isEqualTo(3L);
        assertThat(notifications.get(1).getId()).isEqualTo(2L);
        assertThat(notifications.get(2).getId()).isEqualTo(1L);
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
