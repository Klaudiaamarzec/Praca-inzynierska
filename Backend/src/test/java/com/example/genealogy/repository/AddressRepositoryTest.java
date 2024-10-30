package com.example.genealogy.repository;

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

public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void testFindByCountry() {

        List<Address> addresses = addressRepository.findByCountry("USA");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCountrySmall() {

        List<Address> addresses = addressRepository.findByCountry("usa");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testNotFindByCountry() {

        List<Address> addresses = addressRepository.findByCountry("Poland");
        assertThat(addresses).hasSize(0);
    }

    @Test
    void testFindByVoivodeship() {

        List<Address> addresses = addressRepository.findByVoivodeship("California");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByVoivodeshipSmall() {

        List<Address> addresses = addressRepository.findByVoivodeship("california");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }
    @Test
    void testFindByCommunity() {

        List<Address> addresses = addressRepository.findByCommunity("Los Angeles");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCommunitySmall() {

        List<Address> addresses = addressRepository.findByCommunity("angeles");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testNotFindByCommunity() {

        List<Address> addresses = addressRepository.findByCommunity("Munichuch");
        assertThat(addresses).hasSize(0);
    }

    @Test
    void testFindByCity() {

        List<Address> addresses = addressRepository.findByCity("Los Angeles");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByAddress() {

        List<Address> addresses = addressRepository.findByAddress("Via Nazionale");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(8L);
    }

    @Test
    void testFindByAddress2() {

        List<Address> addresses = addressRepository.findByAddress("via nązionalę");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(8L);
    }

    @Test
    void testFindByPostalCode() {

        List<Address> addresses = addressRepository.findByPostalCode("80331");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(6L);
    }

    @Test
    void testNotFindByPostalCode() {

        List<Address> addresses = addressRepository.findByPostalCode("8031");
        assertThat(addresses).hasSize(0);
    }

    @Test
    void testFindByParish() {

        List<Address> addresses = addressRepository.findByParish("Cathedral");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(10L);
        assertThat(addresses.get(1).getId()).isEqualTo(7L);
    }

    @Test
    void testFindByCoordinates() {

        List<Address> addresses = addressRepository.findByCoordinates(140L, 36L);
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(5L);
    }

    @Test
    void testFindByCoordinates2() {

        List<Address> addresses = addressRepository.findByCoordinates(140L, null);
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(5L);
    }

    @Test
    void testFindByLongitudeBetween() {

        List<Address> addresses = addressRepository.findByLongitudeBetween(-15L, 160L);
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(3L);
        assertThat(addresses.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void testFindByLatitudeBetween() {

        List<Address> addresses = addressRepository.findByLatitudeBetween(34L, 36L);

        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(5L);
        assertThat(addresses.get(2).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCityAndVoivodeship() {

        List<Address> addresses = addressRepository.findByCityAndVoivodeship("los Angeles", "Cąlifornia");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCityAndVoivodeship2() {

        List<Address> addresses = addressRepository.findByCityAndVoivodeship("Rome", "California");
        assertThat(addresses).hasSize(3);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(8L);
        assertThat(addresses.get(2).getId()).isEqualTo(2L);
    }

    @Test
    void testFindByCountryAndPostalCode() {

        List<Address> addresses = addressRepository.findByCountryAndPostalCode("USA", "90001");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testNotFindByCountryAndPostalCode() {

        List<Address> addresses = addressRepository.findByCountryAndPostalCode("usa", "2000");
        assertThat(addresses).hasSize(0);
    }

    @Test
    void testFindByParishAndCity() {

        List<Address> addresses = addressRepository.findByParishAndCity("Cathedral", "Dublin");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(10L);
        assertThat(addresses.get(1).getId()).isEqualTo(7L);
    }

    @Test
    void testFindByParishAndCity2() {

        List<Address> addresses = addressRepository.findByParishAndCity("Patrick", "Angeles");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
        assertThat(addresses.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void testFindByPostalCodeBetween() {

        List<Address> addresses = addressRepository.findByPostalCodeBetween("80000", "90002");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(6L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testIfExists() {

        boolean result = addressRepository.addressExists(null, null, null, "Dublin", null, null, "Christ Church Cathedral", null, null, null);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testIfExist2() {
        boolean result = addressRepository.addressExists("Polska", null, null, null, null, null, null, null, null, null);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testIfNotExists() {

        boolean result = addressRepository.addressExists("usa", "Los Angeles", "Los Angeles", "Main St", null, null, null, null, null, null);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testGetAddress() {

        List<Address> addresses = addressRepository.getAddress("USa", "California", "Angeles", "Main St");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testGetAddressNotNull() {

        List<Address> addresses = addressRepository.getAddress("USa", "", "Angeles", "");
        assertThat(addresses).hasSize(1);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void testGetAddressesByParam() {

        List<Address> addresses = addressRepository.getAddressesByParam("Los Angeles");
        assertThat(addresses).hasSize(2);

        // ID Check
        assertThat(addresses.get(0).getId()).isEqualTo(1L);
        assertThat(addresses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testGetAddressesByAllParams() {

        Address addresses = addressRepository.getAddressByAllParams("USA",
                "california",
                null,
                "Los angeles",
                null,
                null,
                null,
                null,
                "ST",
                null);

        // ID Check
        assertThat(addresses.getId()).isEqualTo(2L);
    }
}
