package com.example.genealogy.repository;

import com.example.genealogy.model.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class DateRepositoryTest {

    @Autowired
    private DateRepository dateRepository;

    @Test
    void testFindDate() {

        // Znalezienie konkretnej daty

        Date foundDate =  dateRepository.findDate(13, 11, 2017);

        // Sprawdzenie, czy data została znaleziona
        assertThat(foundDate).isNotNull();

        // Sprawdzenie, czy data ma odpowiednie id
        assertThat(foundDate.getId()).isEqualTo(2);

        // Sprawdzenie, czy data ma odpowiednie wartości
        assertThat(foundDate.getDay()).isEqualTo(13);
        assertThat(foundDate.getMonth()).isEqualTo(11);
        assertThat(foundDate.getYear()).isEqualTo(2017);

    }

    @Test
    void testFindDates() {

        // Znalezienie listy dat spełniających kryteria

        List<Date> dateList = dateRepository.findDates(19, 11, 2003);

        assertThat(dateList).hasSize(3);

        // Sprawdzenie, czy wszystkie daty mają odpowiednie ID
        assertThat(dateList)
                .extracting(Date::getId)
                .containsExactlyInAnyOrder(6L, 2L, 9L);
    }

    @Test
    void testCheckExist() {

        boolean ifExist = dateRepository.exist(10, 8, 1997);
        assertThat(ifExist).isEqualTo(true);
    }

    @Test
    void testCheckNotExist() {

        boolean ifExist = dateRepository.exist( 19, 11, 2003);
        assertThat(ifExist).isEqualTo(false);
    }
}
