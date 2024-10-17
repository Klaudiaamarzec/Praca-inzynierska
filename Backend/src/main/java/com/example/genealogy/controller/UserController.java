package com.example.genealogy.controller;

import com.example.genealogy.model.User;
import com.example.genealogy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/API/Users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("Genealogist")
    public ResponseEntity<List<User>> getGenealogist() {
        List<User> genealogists = userService.getGenealogists();
        return ResponseEntity.ok(genealogists);
    }
}
