package com.example.genealogy.repository;

import com.example.genealogy.model.PhysicalLocations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class PhysicalLocationRepositoryTest {

    @Autowired
    private PhysicalLocationRepository physicalLocationRepository;

    @Test
    void checkIfExist() {

        boolean result = physicalLocationRepository.existsPhysicalLocation(null, "Księga urodzenia", null, 1L, 1L, 1L);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testFindAllByUserId() {

        List<PhysicalLocations> physicalLocations = physicalLocationRepository.findAllByUserId(1L);
        assertThat(physicalLocations).hasSize(2);

        // IDs check
        assertThat(physicalLocations.get(0).getId()).isEqualTo(1L);
        assertThat(physicalLocations.get(1).getId()).isEqualTo(3L);

    }

    @Test
    void testFindOriginal() {

        List<PhysicalLocations> physicalLocations = physicalLocationRepository.findOriginal();
        assertThat(physicalLocations).hasSize(2);

        // IDs check
        assertThat(physicalLocations.get(0).getId()).isEqualTo(1L);
        assertThat(physicalLocations.get(1).getId()).isEqualTo(4L);
    }

    @Test
    void testFindNotOriginal() {

        List<PhysicalLocations> physicalLocations = physicalLocationRepository.findNotOriginal();
        assertThat(physicalLocations).hasSize(2);

        // IDs check
        assertThat(physicalLocations.get(0).getId()).isEqualTo(2L);
        assertThat(physicalLocations.get(1).getId()).isEqualTo(3L);
    }
}
