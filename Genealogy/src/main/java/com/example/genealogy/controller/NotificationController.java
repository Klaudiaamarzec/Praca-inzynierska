package com.example.genealogy.controller;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Notification;
import com.example.genealogy.model.User;
import com.example.genealogy.service.DocumentService;
import com.example.genealogy.service.NotificationService;

import com.example.genealogy.service.UserService;
import com.example.genealogy.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final DocumentService documentService;
    private final JwtUtil jwtUtil;

    public NotificationController(NotificationService notificationService, UserService userService, DocumentService documentService, JwtUtil jwtUtil) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.documentService = documentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("All")
    public ResponseEntity<?> getNotifications(HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            List<Notification> notifications = notificationService.getAllNotifications();
            return ResponseEntity.ok(notifications);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            Notification notification = notificationService.getNotificationById(id);
            if (notification == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            notification.setDisplayed(true);
            notificationService.saveNotification(notification);

            return ResponseEntity.ok(notification);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @GetMapping("AllNotDisplayed")
    public ResponseEntity<?> getAllNotDisplayedNotifications(HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            List<Notification> notifications = notificationService.findNotificationsNotDisplayed();
            return ResponseEntity.ok(notifications);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @GetMapping("AboutEditing")
    public ResponseEntity<?> getAllNotificationsAboutEditing(HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            List<Notification> notifications = notificationService.findNotificationsAboutEdit();
            return ResponseEntity.ok(notifications);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @GetMapping("AboutAdding")
    public ResponseEntity<?> getAllNotificationsAboutAdding(HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            List<Notification> notifications = notificationService.findNotificationsAboutAdd();
            return ResponseEntity.ok(notifications);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @GetMapping("Search/{parameter}")
    public ResponseEntity<?> searchNotification(@PathVariable String parameter, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            List<Notification> notifications = notificationService.findNotifications(parameter);
            return ResponseEntity.ok(notifications);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @PostMapping("/ApproveUpdate/{id}/")
    public ResponseEntity<String> approveDocumentChanges(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            // Sprawdź, czy użytkownik ma odpowiednie uprawnienia (np. genealog)
            if (currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            Notification notification = notificationService.getNotificationById(id);
            Long oldDocumentID = notification.getDocument().getId();
            Long newDocumentID= notification.getNewDocument().getId();

            // Zatwierdź zmiany w dokumencie

            return documentService.approveChanges(oldDocumentID, newDocumentID);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @PostMapping("/RejectUpdate/{id}")
    public ResponseEntity<String> rejectDocumentChanges(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            // Sprawdź, czy użytkownik ma odpowiednie uprawnienia (np. genealog)
            if (currentUser.getIdRole().getId() != 2) { // Upewnij się, że operator porównania jest poprawny
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            Notification notification = notificationService.getNotificationById(id);
            Document newDocument = notification.getNewDocument();

            // Odrzuć zmiany w dokumencie (usuń nowy dokument)
            boolean deleted = documentService.deleteDocument(newDocument);
            if (!deleted) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Wystąpił błąd podczas usuwania nowego dokumentu.");
            }

            boolean isDeleted = notificationService.deleteNotification(notification);
            if(!isDeleted) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas usuwania powiadomienia");
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Zmiany zostały odrzucone, nowy dokument został usunięty.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            Notification notification = notificationService.getNotificationById(id);
            boolean isDeleted = notificationService.deleteNotification(notification);
            if (!isDeleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Powiadomienie nie zostało znalezione");
            }
            return ResponseEntity.ok("Powiadomienie zostało usunięte");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }
}
