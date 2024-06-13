package com.github.ricardobaumann.vehiclemanager.advices;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ErrorHandler {
    private final ObjectMapper objectMapper;

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
            List<ErrorEntry> fieldErrors
    ) {
        private record ErrorEntry(
                String objectName, String path, String message
        ) {
        }
    }

}
