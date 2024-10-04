package com.example.genealogy.repository;

import com.example.genealogy.model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PhysicalLocationRepository physicalLocationRepository;

    @Test
    void checkIfExist() {
        boolean result = locationRepository.existsLocation(2L,2L);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testFindByPhysicalID() {

        List<Location> locations = locationRepository.findByPhysicalID(2L);
        assertThat(locations).hasSize(1);

        Location location = locations.get(0);
        assertThat(location.getId()).isEqualTo(2L);
    }

    @Test
    void testNotFindByPhysicalID() {

        List<Location> locations = locationRepository.findByPhysicalID(4L);
        assertThat(locations).hasSize(0);
    }

    @Test
    void testFindByUrlID() {

        List<Location> locations = locationRepository.findByUrlID(3L);
        assertThat(locations).hasSize(1);

        Location location = locations.get(0);
        assertThat(location.getId()).isEqualTo(3L);

    }

    @Test
    void testFindLocationByDocument() {

        List<Location> locations = locationRepository.findLocationByDocument(7L);
        assertThat(locations).hasSize(1);

        Location location = locations.get(0);
        assertThat(location.getId()).isEqualTo(3L);
    }

    @Test
    void testNotFindLocationByDocument() {

        List<Location> locations = locationRepository.findLocationByDocument(4L);
        assertThat(locations).hasSize(0);
    }
}
