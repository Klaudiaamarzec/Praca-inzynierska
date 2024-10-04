package com.example.genealogy.service;

import com.example.genealogy.model.Date;

import java.time.LocalDate;
import java.util.List;
public interface DateService {

    // Add or update a date
    boolean saveDate(Date date);

    boolean updateDate(Date date);

    boolean existsById(Date date);
    boolean existDate(int day, int month, int year);

    boolean deleteDate(Date date);

    List<Date> getAllDates();

    List<Date> findDates(int day, int month, int year);

    List<Date> findDatesByDateRange(LocalDate fromDate, LocalDate toDate);
}
