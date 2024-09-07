package com.example.genealogy.serviceimplementation;

import com.example.genealogy.dto.DateDTO;
import com.example.genealogy.mapper.DateMapper;
import com.example.genealogy.model.Date;
import com.example.genealogy.repository.DateRepository;
import com.example.genealogy.service.DateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DateServiceImpl  implements DateService {

    private final DateRepository dateRepository;

    private final DateMapper dateMapper;

    public DateServiceImpl(DateRepository dateRepository, DateMapper dateMapper) {
        this.dateRepository = dateRepository;
        this.dateMapper = dateMapper;
    }

    @Override
    public void saveDate(DateDTO dateDTO) {
        Date date = dateMapper.mapToEntity(dateDTO);
        dateRepository.save(date);
    }

    @Override
    public boolean deleteDate(DateDTO dateDTO) {
        if (dateRepository.existsById(dateDTO.getId())) {
            dateRepository.deleteById(dateDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    public List<Date> findDates(int day, int month, int year) {
        return dateRepository.findDates(day, month,year);
    }

    @Override
    public boolean exist(int day, int month, int year) {
        return dateRepository.exist(day, month, year);
    }

    @Override
    public List<Date> findDatesByDateRange(LocalDate fromDate, LocalDate toDate) {

        int fromYear = fromDate.getYear();
        int fromMonth = fromDate.getMonthValue();
        int fromDay = fromDate.getDayOfMonth();
        int toDay = toDate.getDayOfMonth();
        int toYear = toDate.getYear();
        int toMonth = toDate.getMonthValue();

        return dateRepository.findDatesByDateRange(fromYear, fromMonth, fromDay, toYear, toMonth, toDay);
    }
}
