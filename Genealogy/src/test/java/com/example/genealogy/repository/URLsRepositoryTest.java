package com.example.genealogy.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class URLsRepositoryTest {

    @Autowired
    private URLsRepository urLsRepository;

    @Test
    void checkIfExist() {
        boolean result = urLsRepository.existsURL(1L, "https://www.drive.com/user/catalog", "Zdjęcie aktu z księgi");
        assertThat(result).isEqualTo(true);
    }
}
