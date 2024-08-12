package com.example.genealogy.repository;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long> {

    // Find notifications for some list of Documents (e.g. just confirmed/not confirmed documents)
    @Query("SELECT n FROM Notification n WHERE n.document.id IN :documentIDs ORDER BY n.date DESC")
    List<Notification> findNotificationByDocumentList(@Param("documentIDs") List<Long> documentIDs);

    // Find notifications based on user
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userID ORDER BY n.date DESC")
    List<Notification> findNotificationByUser(@Param("userID") long userID);

    // Find notifications based on title
    @Query("SELECT n FROM Notification n WHERE n.title LIKE %:title% ORDER BY n.date DESC")
    List<Notification> findNotificationByTitle(@Param("title") String title);

    // Find notifications based on title or context
    @Query("SELECT n FROM Notification n WHERE n.title LIKE %:title% OR n.context LIKE %:context% ORDER BY n.date DESC")
    List<Notification> findNotificationByTitleContext(@Param("title") String title, @Param("context") String context);

    // Find notifications by parameter
    @Query("SELECT n FROM Notification n WHERE n.context LIKE %:parameter% OR n.title LIKE %:parameter%")
    List<Notification> findNotificationByParametr(@Param("parameter") String parameter);

    // Find notifications about edit
    @Query("SELECT n FROM Notification n WHERE n.newDocument.id != NULL")
    List<Notification> findNotificationsAboutEdit();

    // Find notifications about adding new document
    @Query("SELECT n FROM Notification n WHERE n.newDocument.id = NULL")
    List<Notification> findNotificationsAboutAdd();

}
