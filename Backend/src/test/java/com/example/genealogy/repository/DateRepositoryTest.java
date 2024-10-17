package com.example.genealogy.repository;

import com.example.genealogy.model.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class DateRepositoryTest {

    @Autowired
    private DateRepository dateRepository;

//    @Test
//    void testFindDate() {
//
//        // Znalezienie konkretnej daty
//
//        Date foundDate =  dateRepository.findDate(13, 11, 2017);
//
//        // Sprawdzenie, czy data została znaleziona
//        assertThat(foundDate).isNotNull();
//
//        // Sprawdzenie, czy data ma odpowiednie id
//        assertThat(foundDate.getId()).isEqualTo(2);
//
//        // Sprawdzenie, czy data ma odpowiednie wartości
//        assertThat(foundDate.getDay()).isEqualTo(13);
//        assertThat(foundDate.getMonth()).isEqualTo(11);
//        assertThat(foundDate.getYear()).isEqualTo(2017);
//
//    }

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

    @Test
    void testFindDatesByDateRange() {

        LocalDate startDate = LocalDate.of(1985, 2, 6);
        LocalDate endDate = LocalDate.of(2015, 11, 2);

        int fromYear = startDate.getYear();
        int fromMonth = startDate.getMonthValue();
        int fromDay = startDate.getDayOfMonth();

        int toYear = endDate.getYear();
        int toMonth = endDate.getMonthValue();
        int toDay = endDate.getDayOfMonth();

        List<Date> dates = dateRepository.findDatesByDateRange(fromYear, fromMonth, fromDay, toYear, toMonth, toDay);
        assertThat(dates).hasSize(5);

        assertThat(dates.get(0).getId()).isEqualTo(1);
        assertThat(dates.get(1).getId()).isEqualTo(4);
        assertThat(dates.get(2).getId()).isEqualTo(6);
        assertThat(dates.get(3).getId()).isEqualTo(7);
        assertThat(dates.get(4).getId()).isEqualTo(9);

    }
}
