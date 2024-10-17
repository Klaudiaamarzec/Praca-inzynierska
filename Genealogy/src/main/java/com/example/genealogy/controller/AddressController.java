package com.example.genealogy.controller;

import com.example.genealogy.model.Address;
import com.example.genealogy.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/API/Addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // 1. Dodawanie adresu
    @PostMapping( "/Add")
    public ResponseEntity<String> addAddress(@Valid @RequestBody Address address) {
        boolean exists = addressService.addressExists(address);

        if(exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Adres już istnieje");
        }

        try {
            boolean isSaved = addressService.saveAddress(address);
            if (isSaved) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Adres został pomyślnie dodany");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas zapisywania adresu");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    // 2. Usuwanie adresu na podstawie ID
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        boolean isDeleted = addressService.deleteAddress(address);
        if (isDeleted) {
            return ResponseEntity.ok("Adres został usunięty");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie udało się usunąć adresu");
        }
    }

    // 3. Pobieranie adresu na podstawie ID
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    // 4. Pobieranie wszystkich adresów
    @GetMapping("/All")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    // 5. Aktualizacja adresu na podstawie ID
    @PutMapping("/Update/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @Valid @RequestBody Address updatedAddress) {

        boolean exists = addressService.existsById(id);

        if (exists) {
            try {

                updatedAddress.setId(id);

                boolean isUpdated = addressService.updateAddress(updatedAddress);
                if (isUpdated) {
                    return ResponseEntity.ok("Adres został pomyślnie zaktualizowany");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wystąpił błąd podczas aktualizacji adresu");
                }

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres nie istnieje.");
        }
    }
}
