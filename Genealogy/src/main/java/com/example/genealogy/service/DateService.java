package com.example.genealogy.service;

import com.example.genealogy.model.Date;

import java.time.LocalDate;
import java.util.List;
public interface DateService {

    boolean existsById(Long id);

    boolean dateExists(Date date);

    Date getDateById(Long id);

    boolean saveDate(Date date);

    boolean updateDate(Date date);

    boolean deleteDate(Date date);

    List<Date> getAllDates();

    List<Date> findDates(int day, int month, int year);

    List<Date> findDatesByDateRange(LocalDate fromDate, LocalDate toDate);
}
