package com.example.genealogy.repository;

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
    List<Notification> findNotificationByUser(@Param("userID") Long userID);

    // Find notifications that are not displayed
    @Query("SELECT n FROM Notification n WHERE n.displayed = false ORDER BY n.date DESC")
    List<Notification> findNotificationsNotDisplayed();

    // Find notifications based on title
    @Query(value = "SELECT * FROM Notification n " +
            "WHERE unaccent(lower(n.title)) LIKE unaccent(lower(concat('%', :title, '%'))) ORDER BY n.date DESC",
            nativeQuery = true)
    List<Notification> findNotificationByTitle(@Param("title") String title);

    // Find notifications based on title or context
    @Query(value = "SELECT * FROM Notification n " +
            "WHERE lower(n.title) LIKE lower(concat('%', :title, '%')) " +
            "AND lower(n.context) LIKE lower(concat('%', :context, '%')) " +
            "ORDER BY n.date DESC",
            nativeQuery = true)
    List<Notification> findNotificationByTitleContext(@Param("title") String title, @Param("context") String context);

    // Find notifications by parameter
    @Query(value = "SELECT * FROM Notification n " +
            "WHERE unaccent(lower(n.context)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "OR unaccent(lower(n.title)) LIKE unaccent(lower(CONCAT('%', :parameter, '%'))) " +
            "ORDER BY n.date DESC",
            nativeQuery = true)
    List<Notification> findNotificationByParameter(@Param("parameter") String parameter);

    // Find notifications about edit
    @Query("SELECT n FROM Notification n WHERE n.newDocument.id != NULL ORDER BY n.date DESC")
    List<Notification> findNotificationsAboutEdit();

    // Find notifications about adding new document
    @Query("SELECT n FROM Notification n WHERE n.newDocument.id IS NULL ORDER BY n.date DESC")
    List<Notification> findNotificationsAboutAdd();

}
