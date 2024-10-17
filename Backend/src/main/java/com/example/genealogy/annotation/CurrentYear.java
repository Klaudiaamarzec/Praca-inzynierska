package com.example.genealogy.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = com.example.genealogy.validator.CurrentYearValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentYear {

    String message() default "Rok nie może być większy niż bieżący rok";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

