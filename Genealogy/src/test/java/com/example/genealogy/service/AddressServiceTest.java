package com.example.genealogy.service;

import com.example.genealogy.model.Address;
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
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Test
    void testSaveAddress() {

        // Given
        Address address = new Address();
        address.setCity("Gdansk");
        address.setCountry("Poland");
        address.setVoivodeship("Pomerania");
        address.setPostalCode("80-001");
        address.setAddress("Street 123");

        // When
        boolean isSaved = addressService.saveAddress(address);

        // Then - check if address has been saved
        assertThat(isSaved).isTrue();
        assertThat(addressService.getAllAddresses()).contains(address);
    }

    @Test
    void testNotSaveAddress() {

        // Given
        Address address = new Address();
        address.setCity("Paris");
        address.setCountry("France");
        address.setLongitude(2L);
        address.setLatitude(49L);

        // When
        boolean isSaved = addressService.saveAddress(address);

        // Then - check if address has not been saved (address already exist)
        assertThat(isSaved).isFalse();
    }
}
