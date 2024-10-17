package com.example.genealogy.controller;

import com.example.genealogy.model.Family;
import com.example.genealogy.service.FamilyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/Family")
public class FamilyController {
    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping("AddFamily")
    public ResponseEntity<?> addFamily(@Valid @RequestBody Family family) {

        boolean isSaved = familyService.saveFamily(family);

        if(!isSaved)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie udało się dodać rodziny");

        return ResponseEntity.status(HttpStatus.OK).body("Rodzina została prawidłowo dodana");
    }
}
