package com.example.genealogy.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonDocumentRepositoryTest {

    @Autowired
    private PersonDocumentRepository personDocumentRepository;

    @Test
    void checkIfExist() {

        boolean result = personDocumentRepository.existsPersonDocument(8L,2L);
        assertThat(result).isEqualTo(true);
    }
}
