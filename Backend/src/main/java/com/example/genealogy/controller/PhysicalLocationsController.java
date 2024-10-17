package com.example.genealogy.controller;

import com.example.genealogy.model.*;
import com.example.genealogy.service.*;
import com.example.genealogy.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/API/PhysicalLocations")
public class PhysicalLocationsController {

    private final DocumentService documentService;
    private final PhysicalLocationService physicalLocationService;
    private final LocalAddressService localAddressService;
    private final UserService userService;
    private final LocationService locationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public PhysicalLocationsController(DocumentService documentService, PhysicalLocationService physicalLocationService, LocalAddressService localAddressService, UserService userService, LocationService locationService, JwtUtil jwtUtil) {
        this.documentService = documentService;
        this.physicalLocationService = physicalLocationService;
        this.localAddressService = localAddressService;
        this.userService = userService;
        this.locationService= locationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("GetMyPhysicalLocations")
    public ResponseEntity<?> getUsersPhysicalLocations(HttpServletRequest request) {

        try {

            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak tokenu JWT lub token jest nieprawidłowy");
            }

            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            return ResponseEntity.ok(physicalLocationService.findAllByUser(currentUser));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @PostMapping("Add/{documentID}")
    public ResponseEntity<?> addPhysicalLocation(@PathVariable Long documentID, @RequestBody PhysicalLocations physicalLocations, HttpServletRequest request) {

        try {

            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak tokenu JWT lub token jest nieprawidłowy");
            }

            String token = authorizationHeader.substring(7); // Pobranie samego tokenu
            String username = jwtUtil.extractUsername(token); // Wyodrębnienie nazwy użytkownika z tokenu

            // Pobierz zalogowanego użytkownika na podstawie wyodrębnionego username
            User currentUser = userService.findByUserName(username);

            physicalLocations.setUser(currentUser);
            physicalLocations.setDate(LocalDate.now());

            Document document = documentService.getDocumentById(documentID);

            if(document.getLocalization() == null) {
                ResponseEntity<?> locationResponse = locationService.addLocalizationToDocument(documentID);

                if (locationResponse.getStatusCode() != HttpStatus.OK) {
                    return locationResponse;
                }

                document = documentService.getDocumentById(documentID);
            }

            physicalLocations.setPhysical(document.getLocalization());

            LocalAddress insertedLocalAddress = physicalLocations.getLocaladdress();

            if(insertedLocalAddress != null) {

                if(localAddressService.localAddressExists(insertedLocalAddress)) {
                    LocalAddress existingLocalAddress = localAddressService.getLocalAddressByAllParams(
                            insertedLocalAddress.getCountry(),
                            insertedLocalAddress.getVoivodeship(),
                            insertedLocalAddress.getCommunity(),
                            insertedLocalAddress.getCity(),
                            insertedLocalAddress.getAddress(),
                            insertedLocalAddress.getPostalCode()
                    );
                    physicalLocations.setLocaladdress(existingLocalAddress);
                }
                else {
                    localAddressService.saveLocalAddress(insertedLocalAddress);
                    physicalLocations.setLocaladdress(insertedLocalAddress);
                }
            }

            boolean isSaved = physicalLocationService.savePhysicalLocation(physicalLocations);
            if(!isSaved) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas dodawania fizycznej lokalizacji");
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body("Fizyczna lokalizacja zostało prawidłowo dodane do dokumentu");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @PutMapping("Update/{id}")
    public ResponseEntity<?> updatePhysicalLocation(@PathVariable Long id, @RequestBody PhysicalLocations updatedPhysicalLocation, HttpServletRequest request) {

        try {

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUserName(username);

            PhysicalLocations existingPhysicalLocation = physicalLocationService.getPhysicalLocationsById(id);

            if(currentUser != existingPhysicalLocation.getUser())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji");

            existingPhysicalLocation.setDate(LocalDate.now());

            if(updatedPhysicalLocation.getIsOriginal() != null)
                existingPhysicalLocation.setIsOriginal(updatedPhysicalLocation.getIsOriginal());

            if(updatedPhysicalLocation.getCondition() != null)
                existingPhysicalLocation.setCondition(updatedPhysicalLocation.getCondition());

            System.out.println("GET TYPE" + updatedPhysicalLocation.getType());

            if(updatedPhysicalLocation.getType() != null)
                existingPhysicalLocation.setType(updatedPhysicalLocation.getType());

            if(updatedPhysicalLocation.getDescription() != null)
                existingPhysicalLocation.setDescription(updatedPhysicalLocation.getDescription());

            LocalAddress insertedLocalAddress = updatedPhysicalLocation.getLocaladdress();
            if(insertedLocalAddress != null) {

                if(localAddressService.localAddressExists(insertedLocalAddress)) {
                    LocalAddress existingLocalAddress = localAddressService.getLocalAddressByAllParams(
                            insertedLocalAddress.getCountry(),
                            insertedLocalAddress.getVoivodeship(),
                            insertedLocalAddress.getCommunity(),
                            insertedLocalAddress.getCity(),
                            insertedLocalAddress.getAddress(),
                            insertedLocalAddress.getPostalCode()
                    );
                    existingPhysicalLocation.setLocaladdress(existingLocalAddress);
                }
                else {
                    localAddressService.saveLocalAddress(insertedLocalAddress);
                    existingPhysicalLocation.setLocaladdress(insertedLocalAddress);
                }
            }

            boolean isSaved = physicalLocationService.savePhysicalLocation(existingPhysicalLocation);
            if(!isSaved) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas aktualizacji fizycznej lokalizacji");
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body("Fizyczna lokalizacja została prawidłowo zaktualizowana");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }

    }
}
