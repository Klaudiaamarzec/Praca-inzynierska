package com.example.genealogy.service;

import com.example.genealogy.model.Address;
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
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Test
    void testExistById() {

        boolean result = addressService.existsById(11L);
        assertThat(result).isTrue();
    }

    @Test
    void testAddressExist() {

        Address address = new Address();
        address.setCountry("Italy");
        address.setCity("Rome");
        address.setAddress("Via Nazionale 10");
        address.setPostalCode("00184");

        boolean result = addressService.addressExists(address);
        assertThat(result).isTrue();
    }

    @Test
    void testAddressNotExist() {

        Address address = new Address();
        address.setCountry("USA");
        address.setCity("Los Angeles");
        address.setAddress("Main St");

        boolean result = addressService.addressExists(address);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveAddress() {

        Address address = new Address();
        address.setCity("Gdansk");
        address.setCountry("Poland");
        address.setVoivodeship("Pomerania");
        address.setPostalCode("80-001");
        address.setAddress("Street 123");

        boolean isSaved = addressService.saveAddress(address);

        assertThat(isSaved).isTrue();
        assertThat(addressService.getAllAddresses()).contains(address);
    }

    @Test
    void testNotSaveAddress() {

        Address address = new Address();
        address.setCity("Paris");
        address.setCountry("France");
        address.setLongitude(2L);
        address.setLatitude(49L);

        boolean isSaved = addressService.saveAddress(address);

        // Check if address has not been saved (address already exist)
        assertThat(isSaved).isFalse();
    }

    @Test
    void testGetAddressById() {

        Address address = addressService.getAddressById(4L);

        assertThat(address).isNotNull();
        assertThat(address.getCountry()).isEqualTo("Japan");
    }

    @Test
    void testNotGetAddressById() {

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            addressService.getAddressById(21L);
        });

        // Możesz również sprawdzić wiadomość wyjątku
        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono adresu o id: " + 21L);
    }

    @Test
    void testUpdateAddress() {
        // Utwórz nowy adres, który będzie aktualizowany
        Address address = new Address();
        address.setCity("Gdansk");
        address.setCountry("Poland");
        address.setVoivodeship("Pomerania");
        address.setPostalCode("80-001");
        address.setAddress("Street 123");

        // Zapisz adres w repozytorium
        addressService.saveAddress(address);

        // Zaktualizuj szczegóły adresu
        address.setCity("Gdynia"); // Nowe miasto
        address.setAddress("Street 456"); // Nowy adres

        // Wywołaj metodę aktualizacji
        boolean isUpdated = addressService.updateAddress(address);

        // Sprawdź, czy aktualizacja była udana
        assertThat(isUpdated).isTrue();

        // Pobierz zaktualizowany adres z repozytorium
        Address updatedAddress = addressService.getAddressById(address.getId());

        // Sprawdź, czy wartości zostały zaktualizowane
        assertThat(updatedAddress.getCity()).isEqualTo("Gdynia");
        assertThat(updatedAddress.getAddress()).isEqualTo("Street 456");
    }

    @Test
    void testDelete() {

        Address addressToDelete = new Address();
        addressToDelete.setId(1L);

        boolean result = addressService.deleteAddress(addressToDelete);

        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            addressService.getAddressById(1L);
        });

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono adresu o id: " + 1L);
    }

    @Test
    void testNotDelete() {

        Address nonExistentAddress = new Address();
        nonExistentAddress.setId(99L);

        boolean result = addressService.deleteAddress(nonExistentAddress);

        assertThat(result).isFalse();
    }

    @Test
    void testGetAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        assertThat(addresses).hasSize(11);
    }

    @Test
    void testFindByCountry() {
        List<Address> addresses = addressService.findByCountry("USA");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByVoivodeship() {
        List<Address> addresses = addressService.findByVoivodeship("california");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCommunity() {
        List<Address> addresses = addressService.findByCommunity("Angeles");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCity() {
        List<Address> addresses = addressService.findByCity("Angeles");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByAddress() {
        List<Address> addresses = addressService.findByAddress("Via Nazionale");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(8L);
    }

    @Test
    void testFindByPostalCode() {
        List<Address> addresses = addressService.findByPostalCode("80331");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(6L);
    }

    @Test
    void testFindByParish() {
        List<Address> addresses = addressService.findByParish("Cathedral");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(7L);
        assertThat(addresses.get(1).getId()).isEqualTo(10L);
    }

    @Test
    void testFindByCoordinates() {

        Long testLongitude = 2L;
        Long testLatitude = 49L;

        List<Address> addresses = addressService.findByCoordinates(testLongitude, testLatitude);

        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(3L);
    }

    @Test
    void testFindByLongitudeBetween() {

        Long minLongitude = -118L;
        Long maxLongitude = 130L;

        List<Address> addresses = addressService.findByLongitudeBetween(minLongitude, maxLongitude);

        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
        assertThat(addresses.get(2).getId()).isEqualTo(3L);
    }

    @Test
    void testFindByLatitudeBetween() {

        Long minLatitude = 35L;
        Long maxLatitude = 50L;

        List<Address> addresses = addressService.findByLatitudeBetween(minLatitude, maxLatitude);

        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(5L);
        assertThat(addresses.get(1).getId()).isEqualTo(3L);
    }

    @Test
    void testFindByPostalCodeBetween() {

        List<Address> addresses = addressService.findByPostalCodeBetween("2000", "90002");

        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(6L);
        assertThat(addresses.get(2).getId()).isEqualTo(9L);
    }

    @Test
    void testSearchAddress1() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, null, null, null, null, null,null, null, null, null, null, null, null);

        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
        assertThat(addresses.get(2).getId()).isEqualTo(16L);
    }

    @Test
    void testSearchAddress2() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, "Los Angeles", null, null, null, null,null, null, null, null, null, null, null);

        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testSearchAddress3() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, null, "Main St", null, null, null, null, null, null, null, null, null, null);

        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(16L);
    }

    @Test
    void testSearchAddress4() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, null, null, null, null, null,null, null, null, -120L, 0L, 32L, 42L);

        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
        assertThat(addresses.get(2).getId()).isEqualTo(16L);
    }

    @Test
    void testSearchAddress5() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, null, "Main St", null, null, null,null, null, null, -120L, 0L, 32L, 42L);

        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(16L);
    }

    @Test
    void testSearchAddress6() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, "Angeles", "Main St", null, null, null, null, null, null, -120L, 0L, 32L, 42L);

        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testSearchAddress7() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, null, "Main St", null, null, null, null, null, null, -120L, 0L, 0L, 20L);

        assertThat(addresses).hasSize(0);
    }

    @Test
    void testSearchAddress8() {

        List<Address> addresses = addressService.searchAddress("USA", "California", null, null, null, null, "2000", "90002", null, null, null, null, null, null, null);

        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testSearchAddress9() {

        List<Address> addresses = addressService.searchAddress(null, null, null, null, null, null, "2000", "90002", null, null, null, null, null, null, null);

        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(6L);
        assertThat(addresses.get(2).getId()).isEqualTo(9L);
    }

    @Test
    void testSearchAddress10() {

        List<Address> addresses = addressService.searchAddress(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(addresses).hasSize(12);
    }
}
