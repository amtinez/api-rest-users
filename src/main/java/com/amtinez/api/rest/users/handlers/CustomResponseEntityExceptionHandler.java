package com.amtinez.api.rest.users.handlers;

import com.amtinez.api.rest.users.validations.errors.Error;
import com.amtinez.api.rest.users.validations.errors.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

/**
 * CustomResponseEntityExceptionHandler is the class that will handle the different exceptions that may occur in our controllers.
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                               final HttpHeaders headers,
                                                               final HttpStatus status,
                                                               final WebRequest request) {
        final List<ValidationError> errors = ex.getBindingResult()
                                               .getAllErrors()
                                               .stream()
                                               .filter(Objects::nonNull)
                                               .map(this::createValidationError)
                                               .collect(Collectors.toList());
        final Error error = Error.builder()
                                 .errors(errors)
                                 .build();
        return new ResponseEntity<>(error, headers, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        final ValidationError validationError = ValidationError.builder()
                                                               .field(ex.getName())
                                                               .message("does not have the correct type")
                                                               .build();
        final Error error = Error.builder()
                                 .errors(Collections.singletonList(validationError))
                                 .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex) {
        final List<ValidationError> errors = ex.getConstraintViolations().stream()
                                               .map(constraintViolation -> {
                                                   final PathImpl path = (PathImpl) constraintViolation.getPropertyPath();
                                                   return ValidationError.builder()
                                                                         .field(path.getLeafNode().getName())
                                                                         .message(constraintViolation.getMessage())
                                                                         .build();
                                               })
                                               .collect(Collectors.toList());
        final Error error = Error.builder()
                                 .errors(errors)
                                 .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    private ValidationError createValidationError(final ObjectError error) {
        return Optional.of(error)
                       .filter(FieldError.class::isInstance)
                       .map(FieldError.class::cast)
                       .map(fieldError -> ValidationError.builder()
                                                         .field(fieldError.getField())
                                                         .message(fieldError.getDefaultMessage())
                                                         .build())
                       .orElseGet(() -> ValidationError.builder()
                                                       .field(constraintCodesMap().getOrDefault(error.getCode(), StringUtils.EMPTY))
                                                       .message(error.getDefaultMessage())
                                                       .build());
    }

    private Map<String, String> constraintCodesMap() {
        return Map.of("EqualPassword", "passwords");
    }

}
