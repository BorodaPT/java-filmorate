package ru.yandex.practicum.validation;

import javax.validation.Constraint;
import javax.validation.constraints.Past;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationMinDateFilms.class)
@Past

public @interface MinDateFilms {
    String message() default "Date must not be before {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default "1895-12-28";
}
