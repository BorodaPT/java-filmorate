package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.exception.DefaultMessageException;
import ru.yandex.practicum.exception.ExceptionDataRequest;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.validation.ValidationErrorResponse;
import ru.yandex.practicum.validation.Violation;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    //валидация
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.error(e.getMessage(), e);
        return new ValidationErrorResponse(violations);
    }


    //ошибка прочее
    @ExceptionHandler(ExceptionDataRequest.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public DefaultMessageException onExceptionDataRequest(ExceptionDataRequest e) {
        return new DefaultMessageException(e.getNameExcept(), e.getMessage());
    }

    //ошибка наличия
    @ExceptionHandler(ExceptionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public DefaultMessageException onExceptionNotFound(ExceptionNotFound e) {
        return new DefaultMessageException(e.getNameExcept(), e.getMessage());
    }

}
