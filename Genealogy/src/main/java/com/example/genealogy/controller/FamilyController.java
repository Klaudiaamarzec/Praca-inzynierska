package com.example.genealogy.controller;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;
import com.example.genealogy.service.FamilyService;
import com.example.genealogy.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/API/Family")
public class FamilyController {
    private final FamilyService familyService;
    private final PersonService personService;

    public FamilyController(FamilyService familyService, PersonService personService) {
        this.familyService = familyService;
        this.personService = personService;
    }

    @PostMapping("AddFamily")
    public ResponseEntity<?> addFamily(@Valid @RequestBody Family family) {

        boolean isSaved = familyService.saveFamily(family);

        if(!isSaved)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie udało się dodać rodziny");

        return ResponseEntity.status(HttpStatus.OK).body("Rodzina została prawidłowo dodana");
    }
}
