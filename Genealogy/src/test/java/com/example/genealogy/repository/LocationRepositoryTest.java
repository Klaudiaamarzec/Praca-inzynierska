package com.example.genealogy.repository;

import com.example.genealogy.model.PhysicalLocations;
import com.example.genealogy.model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    void testFindByPhysicalID() {

//        Optional<PhysicalLocations> optPhysicalLocations = physicalLocationRepository.findById(2L);
//        assertThat(optPhysicalLocations).isPresent();
//
//        PhysicalLocations  physicalLocations = optPhysicalLocations.get();

        List<Location> locations = locationRepository.findByPhysicalID(2L);
        assertThat(locations).hasSize(1);

        Location location = locations.get(0);

        assertThat(location.getId()).isEqualTo(2L);
    }
}
