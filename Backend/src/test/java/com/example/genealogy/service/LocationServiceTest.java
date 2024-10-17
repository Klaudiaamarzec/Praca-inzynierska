package com.example.genealogy.service;

import com.example.genealogy.model.Location;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private DocumentService documentService;

    @Test
    void testExistsById() {
        boolean result = locationService.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testLocationExists() {
        Location location = new Location();
        location.setPhysical(2L);
        location.setUrl(2L);
        boolean result = locationService.locationExists(location);
        assertThat(result).isTrue();
    }

    @Test
    void testLocationNotExists() {
        Location location = new Location();
        location.setPhysical(2L);
        location.setUrl(1L);
        boolean result = locationService.locationExists(location);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveLocation() {
        Location location = new Location();
        location.setPhysical(4L);
        location.setUrl(4L);

        boolean isSaved = locationService.saveLocation(location);
        assertThat(isSaved).isTrue();
        assertThat(locationService.getAllLocations()).contains(location);
    }

    @Test
    void testNotSaveLocation() {
        Location location = new Location();
        location.setPhysical(4L);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> locationService.saveLocation(location));

        assertThat(thrown.getConstraintViolations()).hasSize(1);

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("Pole 'URL' nie może być puste"); // Zmodyfikuj według rzeczywistego komunikatu
    }

    @Test
    void testNotSaveLocationAlreadyExists() {
        Location location = new Location();
        location.setPhysical(2L);
        location.setUrl(2L);

        boolean result = locationService.saveLocation(location);
        assertThat(result).isFalse();
    }

    @Test
    void testGetLocationById() {
        Location location = locationService.getLocationById(1L);
        assertThat(location).isNotNull();
        assertThat(location.getUrl()).isEqualTo(1);
    }

    @Test
    void testNotGetLocationById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> locationService.getLocationById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono lokalizacji o id: " + 99);
    }

    @Test
    void testDeleteLocation() {
        Location locationToDelete = new Location();
        locationToDelete.setId(1L);

        boolean result = locationService.deleteLocation(locationToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> locationService.getLocationById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono lokalizacji o id: " + locationToDelete.getId());
    }

    @Test
    void testNotDeleteLocation() {
        Location nonExistentLocation = new Location();
        nonExistentLocation.setId(99L);

        boolean result = locationService.deleteLocation(nonExistentLocation);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        assertThat(locations).hasSize(3);
    }

    @Test
    void testFindByPhysicalID() {

        List<Location> locations = locationService.findByPhysicalID(2L);

        assertThat(locations).hasSize(1);

        assertThat(locations.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByUrlID() {

        List<Location> locations = locationService.findByUrlID(1L);

        assertThat(locations).hasSize(1);

        assertThat(locations.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void testFindLocationByDocument() {

        List<Location> locations = locationService.findLocationByDocument(documentService.getDocumentById(2L));

        assertThat(locations).hasSize(1);

        assertThat(locations.get(0).getId()).isEqualTo(2L);
    }
}
