package com.example.genealogy.mapper;

import com.example.genealogy.dto.DateDTO;
import com.example.genealogy.model.Date;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {

    public DateDTO mapToDTO(Date date) {

        if (date == null) {
            return null;
        }

        DateDTO dto = new DateDTO();
        dto.setId(date.getId());
        dto.setDay(date.getDay());
        dto.setMonth(date.getMonth());
        dto.setYear(date.getYear());
        return dto;
    }

    public Date mapToEntity(DateDTO dto) {

        if(dto == null) {
            return null;
        }

        Date date = new Date();
        date.setId(dto.getId());
        date.setDay(dto.getDay());
        date.setMonth(dto.getMonth());
        date.setYear(dto.getYear());
        return date;
    }
}
