package com.example.genealogy.controller;

import com.example.genealogy.model.DocumentType;
import com.example.genealogy.model.User;
import com.example.genealogy.service.DocumentTypeService;
import com.example.genealogy.service.UserService;
import com.example.genealogy.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/DocumentTypes")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService, UserService userService, JwtUtil jwtUtil) {

        this.documentTypeService = documentTypeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 1. Dodanie nowego typu

    @PostMapping("/Add")
    public ResponseEntity<String> addDocumentType(@Valid @RequestBody DocumentType documentType, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            if(currentUser.getIdRole().getId() == 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");
            }

            boolean isSaved = documentTypeService.saveDocumentType(documentType);

            if(!isSaved)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie udało się zapisać nowego typu");

            return ResponseEntity.status(HttpStatus.OK).body("Nowy typ został prawidłowo dodany");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    // 2. Pobranie szablonu typu po id

    @GetMapping("GetTemplate/{id}")
    public ResponseEntity<DocumentType> getDocumentType(@PathVariable Integer id) {

        DocumentType documentType = documentTypeService.getDocumentTypeById(id);
        return ResponseEntity.ok(documentType);
    }

    // 3. Aktualizacja typu
}
