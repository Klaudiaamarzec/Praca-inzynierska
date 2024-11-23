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

        ResponseEntity<String> response  = familyService.saveFamily(family);

        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }

        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }
}
