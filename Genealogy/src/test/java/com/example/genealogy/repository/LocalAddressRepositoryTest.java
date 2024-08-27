package com.example.genealogy.repository;

import com.example.genealogy.model.LocalAddress;
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

public class LocalAddressRepositoryTest {

    @Autowired
    private LocalAddressRepository localAddressRepository;

    @Test
    void testFindLocalAddressByCountry() {

        List<LocalAddress> addresses = localAddressRepository.findLocalAddressByCountry("poland");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(3L);
    }

    @Test
    void testNotFindLocalAddressByCountry() {

        List<LocalAddress> addresses = localAddressRepository.findLocalAddressByCountry("polska");
        assertThat(addresses).hasSize(0);
    }

    @Test
    void testFindLocalAddressByVoivodeship() {

        List<LocalAddress> addresses = localAddressRepository.findLocalAddressByVoivodeship("moscow");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(11L);
    }

    @Test
    void testFindLocalAddressByCity() {

        List<LocalAddress> addresses = localAddressRepository.findLocalAddressByCity("Rome");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(6L);
    }

    @Test
    void testNotFindLocalAddressByCity() {

        List<LocalAddress> addresses = localAddressRepository.findLocalAddressByCity("Romene");
        assertThat(addresses).hasSize(0);
    }

    @Test
    void testFindByCountryAndCity() {

        List<LocalAddress> addresses = localAddressRepository.findByCountryAndCity("Poland", "wroclaw");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(3L);
    }

    @Test
    void checkIfExist() {

        boolean result = localAddressRepository.addressExists("Poland", "dolnoslaskie", "wroclaw", null);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void checkIfNotExist() {

        boolean result = localAddressRepository.addressExists("Poland", "zachodniopomo", "wroclaw", null);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testGetAddress() {

        List<LocalAddress> addresses = localAddressRepository.getAddress("Poland", null, null, null);
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(3L);
    }

    @Test
    void testGetWrongAddress() {

        List<LocalAddress> addresses = localAddressRepository.getAddress("Poland", "dolnoslaskie", "szczecin", null);
        assertThat(addresses).hasSize(0);
    }
}
