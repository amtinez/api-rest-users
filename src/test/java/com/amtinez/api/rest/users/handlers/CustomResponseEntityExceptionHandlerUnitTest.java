package com.amtinez.api.rest.users.handlers;

import com.amtinez.api.rest.users.validations.errors.Error;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * JUnit test for {@link CustomResponseEntityExceptionHandler}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomResponseEntityExceptionHandlerUnitTest {

    private static final String TEST_CODE = "testCode";
    private static final String TEST_REAL_CODE = "EqualPassword";
    private static final String TEST_REAL_CODE_CONVERTED = "passwords";
    private static final String TEST_FIELD_NAME = "testFieldName";
    private static final String TEST_CONSTRAINT_MESSAGE = "testConstraintMessage";
    private static final String TEST_METHOD_ARGUMENT_MESSAGE = "testMethodArgumentMessage";
    private static final String TEST_METHOD_ARGUMENT_TYPE_MESSAGE = "does not have the correct type";

    @Mock
    private WebRequest webRequest;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private BeanPropertyBindingResult beanPropertyBindingResult;
    @Mock
    private ObjectError objectError;
    @Mock
    private FieldError fieldError;
    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;
    @Mock
    private ConstraintViolationException constraintViolationException;
    @Mock
    private ConstraintViolationImpl<?> constraintViolation;
    @Mock
    private PathImpl path;
    @Mock
    private NodeImpl node;

    @InjectMocks
    private CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler;

    @Test
    void testHandleMethodArgumentNotValid() {
        when(objectError.getCode()).thenReturn(TEST_REAL_CODE);
        when(objectError.getDefaultMessage()).thenReturn(TEST_METHOD_ARGUMENT_MESSAGE);
        when(beanPropertyBindingResult.getAllErrors()).thenReturn(Collections.singletonList(objectError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(beanPropertyBindingResult);
        final ResponseEntity<Object> responseEntity =
            customResponseEntityExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException,
                                                                              HttpHeaders.EMPTY,
                                                                              HttpStatus.BAD_REQUEST,
                                                                              webRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getClass()).isEqualTo(Error.class);
        final Error validationError = (Error) responseEntity.getBody();
        assertFalse(validationError.getErrors().isEmpty());
        assertThat(validationError.getErrors()).hasSize(1);
        assertThat(validationError.getErrors().get(0).getMessage()).isEqualTo(TEST_METHOD_ARGUMENT_MESSAGE);
        assertThat(validationError.getErrors().get(0).getField()).isEqualTo(TEST_REAL_CODE_CONVERTED);
    }

    @Test
    void testHandleMethodArgumentNotValidConstraintCodeNotExists() {
        when(objectError.getCode()).thenReturn(TEST_CODE);
        when(objectError.getDefaultMessage()).thenReturn(TEST_METHOD_ARGUMENT_MESSAGE);
        when(beanPropertyBindingResult.getAllErrors()).thenReturn(Collections.singletonList(objectError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(beanPropertyBindingResult);
        final ResponseEntity<Object> responseEntity =
            customResponseEntityExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException,
                                                                              HttpHeaders.EMPTY,
                                                                              HttpStatus.BAD_REQUEST,
                                                                              webRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getClass()).isEqualTo(Error.class);
        final Error validationError = (Error) responseEntity.getBody();
        assertFalse(validationError.getErrors().isEmpty());
        assertThat(validationError.getErrors()).hasSize(1);
        assertThat(validationError.getErrors().get(0).getMessage()).isEqualTo(TEST_METHOD_ARGUMENT_MESSAGE);
        assertThat(validationError.getErrors().get(0).getField()).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void testHandleMethodArgumentNotValidFieldError() {
        when(fieldError.getField()).thenReturn(TEST_FIELD_NAME);
        when(fieldError.getDefaultMessage()).thenReturn(TEST_METHOD_ARGUMENT_MESSAGE);
        when(beanPropertyBindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(beanPropertyBindingResult);
        final ResponseEntity<Object> responseEntity =
            customResponseEntityExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException,
                                                                              HttpHeaders.EMPTY,
                                                                              HttpStatus.BAD_REQUEST,
                                                                              webRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getClass()).isEqualTo(Error.class);
        final Error validationError = (Error) responseEntity.getBody();
        assertFalse(validationError.getErrors().isEmpty());
        assertThat(validationError.getErrors()).hasSize(1);
        assertThat(validationError.getErrors().get(0).getMessage()).isEqualTo(TEST_METHOD_ARGUMENT_MESSAGE);
        assertThat(validationError.getErrors().get(0).getField()).isEqualTo(TEST_FIELD_NAME);
    }

    @Test
    void testHandleMethodArgumentTypeMismatch() {
        when(methodArgumentTypeMismatchException.getName()).thenReturn(TEST_FIELD_NAME);
        final ResponseEntity<Object> responseEntity =
            customResponseEntityExceptionHandler.handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getClass()).isEqualTo(Error.class);
        final Error validationError = (Error) responseEntity.getBody();
        assertFalse(validationError.getErrors().isEmpty());
        assertThat(validationError.getErrors()).hasSize(1);
        assertThat(validationError.getErrors().get(0).getMessage()).isEqualTo(TEST_METHOD_ARGUMENT_TYPE_MESSAGE);
        assertThat(validationError.getErrors().get(0).getField()).isEqualTo(TEST_FIELD_NAME);
    }

    @Test
    void testHandleConstraintViolation() {
        when(node.getName()).thenReturn(TEST_FIELD_NAME);
        when(path.getLeafNode()).thenReturn(node);
        when(constraintViolation.getPropertyPath()).thenReturn(path);
        when(constraintViolation.getMessage()).thenReturn(TEST_CONSTRAINT_MESSAGE);
        when(constraintViolationException.getConstraintViolations()).thenReturn(Collections.singleton(constraintViolation));
        final ResponseEntity<Object> responseEntity =
            customResponseEntityExceptionHandler.handleConstraintViolation(constraintViolationException);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getClass()).isEqualTo(Error.class);
        final Error validationError = (Error) responseEntity.getBody();
        assertFalse(validationError.getErrors().isEmpty());
        assertThat(validationError.getErrors()).hasSize(1);
        assertThat(validationError.getErrors().get(0).getMessage()).isEqualTo(TEST_CONSTRAINT_MESSAGE);
        assertThat(validationError.getErrors().get(0).getField()).isEqualTo(TEST_FIELD_NAME);
    }

}
