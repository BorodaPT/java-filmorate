package ru.yandex.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ValidationMinDateFilms implements ConstraintValidator<MinDateFilms, LocalDate> {
    private LocalDate minimumDate;

    @Override
    public void initialize(MinDateFilms constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(minimumDate);
    }
}
