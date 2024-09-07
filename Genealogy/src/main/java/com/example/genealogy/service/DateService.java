package com.example.genealogy.service;

import com.example.genealogy.dto.DateDTO;
import com.example.genealogy.model.Date;

import java.time.LocalDate;
import java.util.List;
public interface DateService {

    // Add or update a date
    void saveDate(DateDTO dateDTO);

    boolean deleteDate(DateDTO dateDTO);

    List<Date> findDates(int day, int month, int year);

    boolean exist(int day, int month, int year);

    List<Date> findDatesByDateRange(LocalDate fromDate, LocalDate toDate);
}
