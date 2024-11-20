package com.example.genealogy.controller;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.URLs;
import com.example.genealogy.model.User;
import com.example.genealogy.service.DocumentService;
import com.example.genealogy.service.LocationService;
import com.example.genealogy.service.URLsService;
import com.example.genealogy.service.UserService;
import com.example.genealogy.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/API/URLs")
public class URLsController {

    private final DocumentService documentService;
    private final LocationService locationService;
    private final URLsService urLsService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired public URLsController(DocumentService documentService, LocationService locationService, URLsService urLsService, UserService userService, JwtUtil jwtUtil) {
        this.documentService = documentService;
        this.locationService = locationService;
        this.urLsService = urLsService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Dodanie adresu URL do dokumentu
    @PostMapping("Add/{documentID}")
    public ResponseEntity<?> addURLToDocument(@PathVariable Long documentID, @RequestBody URLs urL) {

        Document document = documentService.getDocumentById(documentID);

        if(document.getLocalization() == null) {
            ResponseEntity<?> locationResponse = locationService.addLocalizationToDocument(documentID);

            if (locationResponse.getStatusCode() != HttpStatus.OK) {
                return locationResponse;
            }

            document = documentService.getDocumentById(documentID);
        }

        urL.setUrlID(document.getLocalization());

        if (urLsService.urlExists(urL)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Adres URL jest już dodany do wybranego dokumentu");
        }

        boolean isSaved = urLsService.saveURL(urL);

        if(!isSaved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie udało się zapisać adresu URL");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Adres URL został prawidłowo dodany do dokumentu");
    }

    @PutMapping("Update/{id}")
    public ResponseEntity<?> updateURL(@PathVariable Long id, @RequestBody URLs updatedURL, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesja wygasła. Proszę się zalogować ponownie.");
            }

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            URLs existingUrl = urLsService.getURLById(id);

            if(updatedURL.getComment() != null)
                existingUrl.setComment(updatedURL.getComment());

            if(updatedURL.getUrl() != null)
                existingUrl.setUrl(updatedURL.getUrl());
            
            boolean isSaved = urLsService.saveURL(existingUrl);
            
            if(!isSaved) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie udało się zaktualizować adresu URL");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Adres URL został prawidłowo zaktualizowany");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }
}
