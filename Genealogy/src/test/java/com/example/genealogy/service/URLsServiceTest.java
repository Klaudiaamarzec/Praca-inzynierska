package com.example.genealogy.service;

import com.example.genealogy.model.URLs;
import com.example.genealogy.model.Location;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
public class URLsServiceTest {

    @Autowired
    private URLsService urlsService;

    @Autowired
    private LocationService locationService;

    @Test
    void testExistsById() {
        boolean exists = urlsService.existsById(1L);
        assertThat(exists).isTrue();
    }

    @Test
    void testURLExists() {

        URLs urls = new URLs();
        urls.setUrlID(locationService.getLocationById(2L));
        urls.setUrl("https://www.drive.com/user/catalog/kim_kanye_marriage_certificate");
        urls.setComment("Akt małżeństwa Kim Kardashian i Kanye Westa");

        boolean result = urlsService.urlExists(urls);
        assertThat(result).isTrue();
    }

    @Test
    void testURLNotExists() {

        URLs urls = new URLs();
        urls.setUrlID(locationService.getLocationById(2L));
        urls.setUrl("https://www.drive.com/user/catalog/kim_kanye_marriage_certificate");
        urls.setComment("Kim Kardashian");

        boolean result = urlsService.urlExists(urls);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveURLs() {

        URLs urls = new URLs();
        urls.setUrlID(locationService.getLocationById(1L));
        urls.setUrl("https://example.com");
        urls.setComment("Test comment");

        boolean isSaved = urlsService.saveURL(urls);

        assertThat(isSaved).isTrue();
        assertThat(urlsService.getAllURLs()).contains(urls);
    }

    @Test
    void testNotSaveURLsWithEmptyUrl() {

        URLs urls = new URLs();
        urls.setUrlID(locationService.getLocationById(1L));
        urls.setUrl(null);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            urlsService.saveURL(urls);
        });

        assertThat(thrown.getConstraintViolations()).hasSize(1);

        String messages = thrown.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertThat(messages).contains("URL nie może być pusty");
    }

    @Test
    void testGetURLsById() {
        URLs urls = urlsService.getURLById(1L);

        assertThat(urls).isNotNull();
        assertThat(urls.getUrl()).isEqualTo("https://www.drive.com/user/catalog");
    }

    @Test
    void testNotGetURLsById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            urlsService.getURLById(99L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono adresu URL o id: " + 99);
    }

    @Test
    void testUpdateURLs() {
        URLs urls = urlsService.getURLById(1L);
        urls.setUrl("https://updated-url.com");

        boolean isUpdated = urlsService.updateURL(urls);

        assertThat(isUpdated).isTrue();

        URLs updatedURLs = urlsService.getURLById(urls.getId());
        assertThat(updatedURLs.getUrl()).isEqualTo("https://updated-url.com");
    }

    @Test
    void testDeleteURLs() {
        URLs urlsToDelete = new URLs();
        urlsToDelete.setId(1L);

        boolean result = urlsService.deleteURL(urlsToDelete);

        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            urlsService.getURLById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono adresu URL o id: " + 1);
    }

    @Test
    void testNotDeleteURLs() {
        URLs nonExistentURLs = new URLs();
        nonExistentURLs.setId(99L);

        boolean result = urlsService.deleteURL(nonExistentURLs);

        assertThat(result).isFalse();
    }

    @Test
    void testGetAllURLs() {
        List<URLs> urlsList = urlsService.getAllURLs();
        assertThat(urlsList).hasSize(4);
    }
}
