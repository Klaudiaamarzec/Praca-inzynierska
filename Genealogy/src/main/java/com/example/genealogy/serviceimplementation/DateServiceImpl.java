package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Date;
import com.example.genealogy.repository.DateRepository;
import com.example.genealogy.service.DateService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class DateServiceImpl implements DateService {

    private final DateRepository dateRepository;
    private final Validator validator;

    public DateServiceImpl(DateRepository dateRepository, Validator validator) {
        this.dateRepository = dateRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull Date date) {
        return dateRepository.existsById(date.getId());
    }

    @Override
    public boolean existDate(int day, int month, int year) {
        return dateRepository.exist(day, month, year);
    }

    @Override
    public boolean saveDate(@NotNull Date date) {
        if (existDate(date.getDay(), date.getMonth(), date.getYear())) {
            return false;
        }

        validateDate(date);

        try {
            dateRepository.save(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateDate(@NotNull Date date) {
        if (!existsById(date)) {
            return false;
        }

        validateDate(date);

        try {
            dateRepository.save(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteDate(Date date) {
        try {
            if (existsById(date)) {
                dateRepository.deleteById(date.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<Date> getAllDates() {
        return dateRepository.findAll();
    }

    @Override
    public List<Date> findDates(int day, int month, int year) {
        return dateRepository.findDates(day, month, year);
    }

    @Override
    public List<Date> findDatesByDateRange(@NotNull LocalDate fromDate, @NotNull LocalDate toDate) {
        int fromYear = fromDate.getYear();
        int fromMonth = fromDate.getMonthValue();
        int fromDay = fromDate.getDayOfMonth();
        int toDay = toDate.getDayOfMonth();
        int toYear = toDate.getYear();
        int toMonth = toDate.getMonthValue();

        return dateRepository.findDatesByDateRange(fromYear, fromMonth, fromDay, toYear, toMonth, toDay);
    }

    private void validateDate(Date date) {
        Set<ConstraintViolation<Date>> violations = validator.validate(date);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Date> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja daty nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
