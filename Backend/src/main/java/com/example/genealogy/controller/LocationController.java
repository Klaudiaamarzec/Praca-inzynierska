package com.example.genealogy.controller;

import com.example.genealogy.model.LocalAddress;
import com.example.genealogy.model.Location;
import com.example.genealogy.model.Person;
import com.example.genealogy.service.DocumentService;
import com.example.genealogy.service.LocationService;
import com.example.genealogy.service.NotificationService;
import com.example.genealogy.service.UserService;
import com.example.genealogy.token.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/API/Locations")
public class LocationController {

    private final LocationService locationService;
    private final JwtUtil jwtUtil;

    public LocationController(LocationService locationService, JwtUtil jwtUtil) {
        this.locationService = locationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("All")
    public ResponseEntity<List<Location>> getAllPeople() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }
}
