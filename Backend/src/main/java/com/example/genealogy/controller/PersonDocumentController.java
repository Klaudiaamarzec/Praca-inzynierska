package com.example.genealogy.controller;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.model.PersonDocument;
import com.example.genealogy.model.User;
import com.example.genealogy.service.*;
import com.example.genealogy.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/API")
public class PersonDocumentController {

    private final DocumentService documentService;
    private final PersonService personService;
    private final PersonDocumentService personDocumentService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public PersonDocumentController(DocumentService documentService, PersonService personService, PersonDocumentService personDocumentService, UserService userService, JwtUtil jwtUtil) {
        this.documentService = documentService;
        this.personDocumentService = personDocumentService;
        this.personService = personService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Dodawanie osób do dokumentu
    @PostMapping("AddPersonToDocument/{documentID}")
    public ResponseEntity<String> addPeopleToDocument(@PathVariable Long documentID, @RequestBody PersonDocument personDocument) {

        Document document = documentService.getDocumentById(documentID);

        if (document == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dokument nie został znaleziony");
        }

        if (personDocument == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Brak osoby do dodania");
        }

        Person person = personService.getPersonById(personDocument.getPerson().getId());

        personDocument.setDocument(document);
        personDocument.setPerson(person);
        personDocument.setComment(personDocument.getComment());
        personDocument.setX(personDocument.getX());
        personDocument.setY(personDocument.getY());

        if(personDocumentService.personDocumentExists(personDocument)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(person.getName() + " " + person.getSurname() + " jest już dodany/a do dokumentu");
        }

        boolean result = personDocumentService.savePersonDocument(personDocument);

        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Osoba " + person.getName() + " " + person.getSurname() + " została pomyślnie dodana do dokumentu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas dodawania "+person.getName() + " " + person.getSurname()  + " do dokumentu");
        }

    }

    // Dodawanie dokumentów do osoby
    @PostMapping("AddDocumentToPerson/{personID}")
    public ResponseEntity<String> addDocumentsToPerson(@PathVariable Long personID, @RequestBody PersonDocument personDocumentData) {

        Person person = personService.getPersonById(personID);

        if(person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Osoba nie została znaleziona");
        }

        Document document = documentService.getDocumentById(personDocumentData.getDocument().getId());

        if (document == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dokument nie został znaleziony");
        }

        PersonDocument personDocument = new PersonDocument();
        personDocument.setPerson(person);
        personDocument.setDocument(document);
        personDocument.setComment(personDocumentData.getComment());
        personDocument.setX(personDocumentData.getX());
        personDocument.setY(personDocumentData.getY());

        try {
            personDocumentService.savePersonDocument(personDocument);
            return ResponseEntity.status(HttpStatus.OK).body("Dokument został pomyślnie dodany do osoby");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas dodawania dokumentu do osoby");
        }
    }

    // Usunięcie danej osoby z dokumentu
    @DeleteMapping("DeletePerson/{documentID}/{personID}")
    public ResponseEntity<String> deletePersonFromDocument(@PathVariable Long documentID, @PathVariable Long personID, HttpServletRequest request) {

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

            PersonDocument personDocument = personDocumentService.getPersonDocument(documentID, personID);

            boolean isDeleted = personDocumentService.deletePersonDocument(personDocument);
            if(isDeleted) {
                return ResponseEntity.ok("Osoba została prawidłowo usunieta z dokumentu");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie udało się usunąć osoby z dokumentu");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

}
