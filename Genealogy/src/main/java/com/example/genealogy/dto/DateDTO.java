package com.example.genealogy.dto;

import com.example.genealogy.annotation.CurrentYear;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DateDTO {

    private long id;

    @NotBlank(message = "Pole 'Rok' nie może być puste")
    @Min(value = 1000, message = "Rok nie może być wcześniejszy niż 1000")
    @CurrentYear(message = "Rok nie może być większy niż bieżący rok")
    private Integer year;

    @Min(value = 1, message = "Miesiąc musi być pomiędzy 1 a 12")
    @Max(value = 12, message = "Miesiąc musi być pomiędzy 1 a 12")
    private Integer month;

    @Min(value = 1, message = "Dzień musi być pomiędzy 1 a 31")
    @Max(value = 31, message = "Dzień musi być pomiędzy 1 a 31")
    private Integer day;
}
