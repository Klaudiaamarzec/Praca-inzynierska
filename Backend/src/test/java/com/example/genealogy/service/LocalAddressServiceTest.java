package com.example.genealogy.service;

import com.example.genealogy.model.LocalAddress;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocalAddressServiceTest {

    @Autowired
    private LocalAddressService localAddressService;

    @Test
    void testExistsById() {
        boolean result = localAddressService.existsById(7L);
        assertThat(result).isTrue();
    }

    @Test
    void testLocalAddressExists() {
        LocalAddress address = new LocalAddress();
        address.setCountry("Poland");
        address.setVoivodeship("Zachodniopomorskie");
        address.setCity("Szczecin");
        address.setAddress("Słowackiego 2/6");
        address.setPostalCode("71100");
        boolean result = localAddressService.localAddressExists(address);
        assertThat(result).isTrue();
    }

    @Test
    void testLocalAddressNotExists() {
        LocalAddress address = new LocalAddress();
        address.setAddress("Unknown Street");
        address.setCity("Unknown City");
        boolean result = localAddressService.localAddressExists(address);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveLocalAddress() {
        LocalAddress address = new LocalAddress();
        address.setAddress("New Street");
        address.setCity("New City");

        boolean isSaved = localAddressService.saveLocalAddress(address);
        assertThat(isSaved).isTrue();
        assertThat(localAddressService.getAllLocalAddresses()).contains(address);
    }

    @Test
    void testNotSaveLocalAddressAlreadyExists() {
        LocalAddress address = new LocalAddress();
        address.setCountry("Poland");
        address.setVoivodeship("Zachodniopomorskie");
        address.setCity("Szczecin");
        address.setAddress("Słowackiego 2/6");
        address.setPostalCode("71100");

        boolean result = localAddressService.saveLocalAddress(address);
        assertThat(result).isFalse();
    }

    @Test
    void testGetLocalAddressById() {
        LocalAddress address = localAddressService.getLocalAddressById(5L);
        assertThat(address).isNotNull();
        assertThat(address.getAddress()).isEqualTo("Champs-Élysées 92");
    }

    @Test
    void testNotGetLocalAddressById() {
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> localAddressService.getLocalAddressById(99L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono lokalnego adresu o id: " + 99);
    }

    @Test
    void testUpdateLocalAddress() {
        LocalAddress address = localAddressService.getLocalAddressById(1L);
        address.setAddress("Updated Street");

        boolean isUpdated = localAddressService.updateLocalAddress(address);
        assertThat(isUpdated).isTrue();

        LocalAddress updatedAddress = localAddressService.getLocalAddressById(address.getId());
        assertThat(updatedAddress.getAddress()).isEqualTo("Updated Street");
    }

    @Test
    void testDeleteLocalAddress() {
        LocalAddress addressToDelete = new LocalAddress();
        addressToDelete.setId(1L);

        boolean result = localAddressService.deleteLocalAddress(addressToDelete);
        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> localAddressService.getLocalAddressById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono lokalnego adresu o id: " + addressToDelete.getId());
    }

    @Test
    void testNotDeleteLocalAddress() {
        LocalAddress nonExistentAddress = new LocalAddress();
        nonExistentAddress.setId(99L);

        boolean result = localAddressService.deleteLocalAddress(nonExistentAddress);
        assertThat(result).isFalse();
    }

    @Test
    void testGetAllLocalAddresses() {
        List<LocalAddress> addresses = localAddressService.getAllLocalAddresses();
        assertThat(addresses).hasSize(13);
    }

    @Test
    void testFindLocalAddressByCountry() {

        List<LocalAddress> localAddresses = localAddressService.findLocalAddressByCountry("Poland");

        assertThat(localAddresses).hasSize(2);

        assertThat(localAddresses.get(0).getId()).isEqualTo(2L);
        assertThat(localAddresses.get(1).getId()).isEqualTo(3L);
    }

    @Test
    void testFindLocalAddressByVoivodeship() {

        List<LocalAddress> localAddresses = localAddressService.findLocalAddressByVoivodeship("Moscow");

        assertThat(localAddresses).hasSize(1);

        assertThat(localAddresses.get(0).getId()).isEqualTo(11L);
    }

    @Test
    void testFindLocalAddressByCity() {

        List<LocalAddress> localAddresses = localAddressService.findLocalAddressByCity("Szczecin");

        assertThat(localAddresses).hasSize(1);

        assertThat(localAddresses.get(0).getId()).isEqualTo(3L);
    }

    @Test
    void testSearchLocalAddress() {

        List<LocalAddress> localAddresses = localAddressService.searchLocalAddress("Poland",null,null);

        assertThat(localAddresses).hasSize(2);

        assertThat(localAddresses.get(0).getId()).isEqualTo(3L);
        assertThat(localAddresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testSearchLocalAddress2() {

        List<LocalAddress> localAddresses = localAddressService.searchLocalAddress("Poland","zachodniopomorskie",null);

        assertThat(localAddresses).hasSize(2);

        assertThat(localAddresses.get(0).getId()).isEqualTo(3L);
        assertThat(localAddresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testSearchLocalAddress3() {

        List<LocalAddress> localAddresses = localAddressService.searchLocalAddress("Poland","zachodniopomorskie","kołobrzeg");

        assertThat(localAddresses).hasSize(1);

        assertThat(localAddresses.get(0).getId()).isEqualTo(2L);
    }
}
