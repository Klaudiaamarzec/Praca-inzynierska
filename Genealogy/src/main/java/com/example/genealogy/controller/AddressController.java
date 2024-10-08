package com.example.genealogy.controller;

import com.example.genealogy.model.Address;
import com.example.genealogy.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        boolean isSaved = addressService.saveAddress(address);
        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Adres został pomyślnie dodany.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Adres już istnieje lub wystąpił błąd.");
        }
    }

    // 2. Usuwanie adresu na podstawie ID
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        boolean isDeleted = addressService.deleteAddress(address);
        if (isDeleted) {
            return ResponseEntity.ok("Adres został usunięty.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres o podanym ID nie istnieje.");
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

    // 5. Szukanie adresu na podstawie parametrów
    @GetMapping("/Search")
    public ResponseEntity<List<Address>> searchAddress(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String voivodeship,
            @RequestParam(required = false) String community,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String startPostalCode,
            @RequestParam(required = false) String endPostalCode,
            @RequestParam(required = false) String parish,
            @RequestParam(required = false) Long longitude,
            @RequestParam(required = false) Long latitude,
            @RequestParam(required = false) Long minLongitude,
            @RequestParam(required = false) Long maxLongitude,
            @RequestParam(required = false) Long minLatitude,
            @RequestParam(required = false) Long maxLatitude) {

        List<Address> addresses = addressService.searchAddress(country, voivodeship, community, city, address,
                postalCode, startPostalCode, endPostalCode, parish, longitude, latitude, minLongitude, maxLongitude, minLatitude, maxLatitude);
        return ResponseEntity.ok(addresses);
    }

    // 6. Aktualizacja adresu na podstawie ID
    @PutMapping("/Update/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @Valid @RequestBody Address updatedAddress) {

        // Ustaw ID w obiekcie updatedAddress, aby upewnić się, że aktualizujemy właściwy rekord
        updatedAddress.setId(id);

        // Wywołaj metodę update z serwisu
        boolean isUpdated = addressService.updateAddress(updatedAddress);
        if (isUpdated) {
            return ResponseEntity.ok("Adres został pomyślnie zaktualizowany.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres o podanym ID nie istnieje lub wystąpił błąd.");
        }
    }
}
