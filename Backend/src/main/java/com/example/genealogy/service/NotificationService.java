package com.example.genealogy.service;

import com.example.genealogy.model.Notification;
import com.example.genealogy.model.User;

import java.util.List;

public interface NotificationService {

    boolean existsById(Long id);

    boolean notificationExists(Notification notification);

    Notification getNotificationById(Long id);

    boolean saveNotification(Notification notification);

    boolean updateNotification(Notification notification);

    boolean deleteNotification(Notification notification);

    boolean deleteByDocumentID(Long documentID);

    List<Notification> getAllNotifications();

    List<Notification> findNotificationByDocumentID(Long documentID);

    List<Notification> findNotificationByUser(User user);

    List<Notification> findNotificationsNotDisplayed();

    List<Notification> findNotifications(String parameter);

    List<Notification> findNotificationsAboutEdit();

    List<Notification> findNotificationsAboutAdd();
}
