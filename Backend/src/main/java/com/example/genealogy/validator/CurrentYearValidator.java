package com.example.genealogy.validator;

import com.example.genealogy.annotation.CurrentYear;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Year;

public class CurrentYearValidator implements ConstraintValidator<CurrentYear, Integer> {

    @Override
    public void initialize(CurrentYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return false;
        }
        int currentYear = Year.now().getValue();
        return year <= currentYear;
    }
}
