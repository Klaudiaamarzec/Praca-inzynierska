package com.example.genealogy.service;

import com.example.genealogy.model.Date;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DateServiceTest {

    @Autowired
    private DateService dateService;

    @Test
    void testExistById() {

        boolean result = dateService.existsById(10L);
        assertThat(result).isTrue();
    }

    @Test
    void testDateExist() {

        Date date = new Date();
        date.setYear(2017);
        date.setMonth(11);
        date.setDay(13);

        boolean result = dateService.dateExists(date);
        assertThat(result).isTrue();
    }

    @Test
    void testDateNotExist() {

        Date date = new Date();
        date.setYear(2003);
        date.setMonth(1);
        date.setDay(1);

        boolean result = dateService.dateExists(date);
        assertThat(result).isFalse();
    }

    @Test
    void testSaveDate() {

        Date date = new Date();
        date.setYear(2003);
        date.setMonth(1);
        date.setDay(1);

        boolean isSaved = dateService.saveDate(date);

        assertThat(isSaved).isTrue();
        assertThat(dateService.getAllDates()).contains(date);
    }

    @Test
    void testNotSaveDate() {

        Date date = new Date();
        date.setYear(2017);
        date.setMonth(11);
        date.setDay(13);

        boolean isSaved = dateService.saveDate(date);

        assertThat(isSaved).isFalse();
    }

    @Test
    void testGetDateById() {

        Date date = dateService.getDateById(5L);

        assertThat(date).isNotNull();
        assertThat(date.getYear()).isEqualTo(2016);
    }

    @Test
    void testNotGetDateById() {

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> dateService.getDateById(22L));

        // Możesz również sprawdzić wiadomość wyjątku
        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono daty o id: " + 22L);
    }

    @Test
    void testUpdateDate() {
        Date date = new Date();
        date.setYear(2003);
        date.setMonth(1);
        date.setDay(1);

        dateService.saveDate(date);

        date.setYear(2024);
        date.setMonth(10);

        boolean isUpdated = dateService.updateDate(date);
        assertThat(isUpdated).isTrue();

        Date updatedDate = dateService.getDateById(date.getId());

        assertThat(updatedDate.getYear()).isEqualTo(2024);
        assertThat(updatedDate.getMonth()).isEqualTo(10);
    }

    @Test
    void testDeleteDate() {

        Date dateToDelete = new Date();
        dateToDelete.setId(1L);

        boolean result = dateService.deleteDate(dateToDelete);

        assertThat(result).isTrue();

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> dateService.getDateById(1L));

        assertThat(thrown.getMessage()).isEqualTo("Nie znaleziono daty o id: " + dateToDelete.getId());
    }

    @Test
    void testNotDeleteDate() {

        Date nonExistentDate = new Date();
        nonExistentDate.setId(99L);

        boolean result = dateService.deleteDate(nonExistentDate);

        assertThat(result).isFalse();
    }

    @Test
    void testGetAllDates() {

        List<Date> dates = dateService.getAllDates();
        assertThat(dates).hasSize(10);
    }

    @Test
    void testFindDates() {

        int day = 13;
        int month = 11;
        int year = 2017;

        List<Date> dates = dateService.findDates(day, month, year);

        assertThat(dates).isNotEmpty();
        assertThat(dates.get(0).getId()).isEqualTo(2);
    }

    @Test
    void testFindDatesByDateRange() {

        LocalDate fromDate = LocalDate.of(1999, 1, 1);
        LocalDate toDate = LocalDate.of(2018, 12, 31);

        List<Date> dates = dateService.findDatesByDateRange(fromDate, toDate);

        assertThat(dates).hasSize(5);
        assertThat(dates.get(0).getId()).isEqualTo(2L);
        assertThat(dates.get(1).getId()).isEqualTo(4L);
        assertThat(dates.get(2).getId()).isEqualTo(5L);
        assertThat(dates.get(3).getId()).isEqualTo(6L);
        assertThat(dates.get(4).getId()).isEqualTo(7L);
    }

    @Test
    void testFindDatesByDateRange2() {

        LocalDate fromDate = LocalDate.of(2017, 12, 1);
        LocalDate toDate = LocalDate.of(2020, 12, 31);

        List<Date> dates = dateService.findDatesByDateRange(fromDate, toDate);

        assertThat(dates).hasSize(0);
    }

}
