package com.github.ricardobaumann.vehiclemanager.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ErrorHandler {
    private final Map<String, String> dbKeyMap = Map.of(
            "brands_name_key", "Duplicated brand name",
            "uk_model_brand_name", "Duplicated model"
    );

    private final Map<String, String> dbFkMap = Map.of(
            "fk_vehicle_model", "The model is still linked to a vehicle",
            "fk_model_brand", "The brand is still linked to a model"
    );

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Error dbConstraintError(DataIntegrityViolationException exception) {
        if (exception.getCause() instanceof ConstraintViolationException constraintViolationException) {
            log.info("Handling constraint name: {}", constraintViolationException.getConstraintName());
            return new Error(BAD_REQUEST.value(),
                    dbKeyMap.getOrDefault(
                            constraintViolationException.getConstraintName(), "Unknown constraint error"),
                    null);
        }
        String message = exception.getCause().getMessage();
        log.info("Handling message: {}", message);
        return dbFkMap.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey())).map(Map.Entry::getValue)
                .map(description -> new Error(BAD_REQUEST.value(), description, null))
                .findAny().orElseGet(() -> new Error(BAD_REQUEST.value(), "Unknown constraint error", null));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Error entityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return new Error(BAD_REQUEST.value(), entityNotFoundException.getMessage(), null);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        return new Error(BAD_REQUEST.value(), "validation error",
                fieldErrors.stream().map(fieldError -> new Error.ErrorEntry(
                                fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()));
    }

    public record Error(
            int status,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL) List<ErrorEntry> fieldErrors
    ) {
        private record ErrorEntry(
                String objectName, String path, String message
        ) {
        }
    }

}
