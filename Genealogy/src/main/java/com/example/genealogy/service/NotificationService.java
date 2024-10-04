package com.example.genealogy.service;

import com.example.genealogy.model.Notification;
import com.example.genealogy.model.User;

import java.util.List;

public interface NotificationService {

    boolean saveNotification(Notification notification);

    boolean updateNotification(Notification notification);

    boolean existsById(Notification notification);

    boolean notificationExist(Notification notification);

    boolean deleteNotification(Notification notification);

    List<Notification> getAllNotifications();

    List<Notification> findNotificationByDocumentList(List<Long> documentIDs);

    List<Notification> findNotificationByUser(User user);

    List<Notification> findNotificationsNotDisplayed();

    List<Notification> findNotifications(String parameter);

    List<Notification> findNotificationsAboutEdit();

    List<Notification> findNotificationsAboutAdd();
}
