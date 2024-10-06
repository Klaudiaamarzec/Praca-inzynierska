package com.example.genealogy.service;

import com.example.genealogy.model.Notification;
import com.example.genealogy.model.PhysicalLocations;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PhysicalLocationsServiceTest {

    @Autowired
    private PhysicalLocationService physicalLocationService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocalAddressService localAddressService;

    @Autowired
    private UserService userService;

    @Test
    void testExistById() {
        boolean result = physicalLocationService.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testPhysicalLocationsExists() {

        PhysicalLocations physicalLocation = new PhysicalLocations();
        physicalLocation.setDate(LocalDate.of(2006,7,15));
        physicalLocation.setIsOriginal(false);
        physicalLocation.setType("Skan księgi urodzenia");
        physicalLocation.setPhysical(locationService.getLocationById(1L));
        physicalLocation.setLocaladdress(localAddressService.getLocalAddressById(2L));
        physicalLocation.setUser(userService.getUserById(5L));

        boolean result = physicalLocationService.physicalLocationExists(physicalLocation);
        assertThat(result).isTrue();
    }

    @Test
    void testPhysicalLocationsNotExist() {

        PhysicalLocations physicalLocation = new PhysicalLocations();
        physicalLocation.setDate(LocalDate.of(2006,7,15));
        physicalLocation.setIsOriginal(true);
        physicalLocation.setType("księga urodzenia");
        physicalLocation.setPhysical(locationService.getLocationById(1L));
        physicalLocation.setLocaladdress(localAddressService.getLocalAddressById(2L));
        physicalLocation.setUser(userService.getUserById(5L));

        boolean result = physicalLocationService.physicalLocationExists(physicalLocation);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveLocation() {

        PhysicalLocations physicalLocation = new PhysicalLocations();
        physicalLocation.setDate(LocalDate.of(2008,7,31));
        physicalLocation.setIsOriginal(true);
        physicalLocation.setType("księga urodzenia");
        physicalLocation.setPhysical(locationService.getLocationById(2L));
        physicalLocation.setLocaladdress(localAddressService.getLocalAddressById(3L));
        physicalLocation.setUser(userService.getUserById(2L));

        boolean isSaved = physicalLocationService.savePhysicalLocation(physicalLocation);

        assertThat(isSaved).isTrue();
        assertThat(physicalLocationService.getAllPhysicalLocations()).contains(physicalLocation);
    }

    @Test
    void testNotSaveLocation() {

        PhysicalLocations physicalLocation = new PhysicalLocations();
        physicalLocation.setDate(LocalDate.of(2006,7,15));
        physicalLocation.setIsOriginal(false);
        physicalLocation.setType("Skan księgi urodzenia");
        physicalLocation.setPhysical(locationService.getLocationById(1L));
        physicalLocation.setLocaladdress(localAddressService.getLocalAddressById(2L));
        physicalLocation.setUser(userService.getUserById(5L));

        boolean isSaved = physicalLocationService.savePhysicalLocation(physicalLocation);
        assertThat(isSaved).isFalse();
    }

    @Test
    void testNotSaveLocation2() {

        PhysicalLocations physicalLocation = new PhysicalLocations();
        physicalLocation.setIsOriginal(false);
        physicalLocation.setType("Skan księgi urodzenia");
        physicalLocation.setPhysical(locationService.getLocationById(1L));
        physicalLocation.setLocaladdress(localAddressService.getLocalAddressById(2L));
        physicalLocation.setUser(userService.getUserById(5L));

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            physicalLocationService.savePhysicalLocation(physicalLocation);
        });

        // Sprawdzenie czy wyjątek zawiera odpowiednią wiadomość walidacyjną
        assertThat(thrown.getConstraintViolations()).hasSize(1); // Weryfikacja liczby błędów walidacji

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Data nie może być pusta");
    }

    @Test
    void testGetLocationById() {
        PhysicalLocations physicalLocations = physicalLocationService.getPhysicalLocationsById(3L);

        assertThat(physicalLocations).isNotNull();
        assertThat(physicalLocations.getType()).isEqualTo("Kopia wycinka z księgi");
    }

    @Test
    void testNotGetLocationById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            physicalLocationService.getPhysicalLocationsById(99L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono fizycznego adresu o id: " + 99L);
    }

    @Test
    void testUpdateLocation() {

        PhysicalLocations physicalLocation = physicalLocationService.getPhysicalLocationsById(2L);
        physicalLocation.setType("Nowy typ");

        physicalLocationService.savePhysicalLocation(physicalLocation);

        boolean isUpdated = physicalLocationService.updatePhysicalLocation(physicalLocation);
        assertThat(isUpdated).isTrue();

        // Pobierz zaktualizowaną lokalizację
        PhysicalLocations updatedLocation = physicalLocationService.getPhysicalLocationsById(physicalLocation.getId());

        // Sprawdź, czy wartości zostały zaktualizowane
        assertThat(updatedLocation.getType()).isEqualTo("Nowy typ");
    }

    @Test
    void testDeleteLocation() {

        PhysicalLocations physicalLocationToDelete = new PhysicalLocations();
        physicalLocationToDelete.setId(1L);

        boolean result = physicalLocationService.deletePhysicalLocation(physicalLocationToDelete);

        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            physicalLocationService.getPhysicalLocationsById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono fizycznego adresu o id: " + 1L);
    }

    @Test
    void testNotDeleteLocation() {
        PhysicalLocations physicalLocationToDelete = new PhysicalLocations();
        physicalLocationToDelete.setId(99L);

        boolean result = physicalLocationService.deletePhysicalLocation(physicalLocationToDelete);

        assertThat(result).isFalse();
    }

    @Test
    void testGetAllLocations() {
        List<PhysicalLocations> physicalLocations = physicalLocationService.getAllPhysicalLocations();
        assertThat(physicalLocations).hasSize(4);
    }

    @Test
    void testFindAllByUser() {

        List<PhysicalLocations> physicalLocations = physicalLocationService.findAllByUser(userService.getUserById(1L));

        assertThat(physicalLocations).hasSize(2);

        assertThat(physicalLocations)
                .extracting(PhysicalLocations::getId)
                .containsExactly(1L, 3L);
    }

    @Test
    void testFindOriginal() {

        List<PhysicalLocations> physicalLocations = physicalLocationService.findOriginal();

        assertThat(physicalLocations).hasSize(2);

        assertThat(physicalLocations)
                .extracting(PhysicalLocations::getId)
                .containsExactly(1L, 4L);
    }

    @Test
    void testFindNotOriginal() {

        List<PhysicalLocations> physicalLocations = physicalLocationService.findNotOriginal();

        assertThat(physicalLocations).hasSize(2);

        assertThat(physicalLocations)
                .extracting(PhysicalLocations::getId)
                .containsExactly(2L, 3L);
    }
}
