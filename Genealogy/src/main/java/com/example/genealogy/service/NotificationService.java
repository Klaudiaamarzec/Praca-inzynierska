package com.example.genealogy.service;

import com.example.genealogy.model.Notification;
import com.example.genealogy.model.User;

import java.util.List;

public interface NotificationService {

    boolean saveNotification(Notification notification);

    boolean updateNotification(Notification notification);

    boolean existsById(long id);

    boolean deleteNotification(Notification notification);

    List<Notification> getAllnotifications();

    List<Notification> findNotificationByDocumentList(List<Long> documentIDs);

    List<Notification> findNotificationByUser(User user);

    List<Notification> findNotificationsNotDisplayed();

    List<Notification> findNotifications(String parameter);

    List<Notification> findNotificationsAboutEdit();

    List<Notification> findNotificationsAboutAdd();
}
