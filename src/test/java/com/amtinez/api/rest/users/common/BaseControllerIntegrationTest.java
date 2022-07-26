package com.amtinez.api.rest.users.common;

import com.amtinez.api.rest.users.validations.errors.Error;
import com.amtinez.api.rest.users.validations.errors.FieldError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseControllerIntegrationTest {

    protected String createFieldError(final String field, final String message) throws JsonProcessingException {
        return getJson(Error.builder()
                            .errors(List.of(FieldError.builder()
                                                      .field(field)
                                                      .message(message)
                                                      .build()))
                            .build());
    }

    protected String getJson(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
