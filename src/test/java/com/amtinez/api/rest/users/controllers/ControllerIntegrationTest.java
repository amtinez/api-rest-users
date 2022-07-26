package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.validations.errors.Error;
import com.amtinez.api.rest.users.validations.errors.FieldError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface ControllerIntegrationTest {

    default String createFieldError(final String field, final String message) throws JsonProcessingException {
        return getJson(Error.builder()
                            .errors(List.of(FieldError.builder()
                                                      .field(field)
                                                      .message(message)
                                                      .build()))
                            .build());
    }

    default String getJson(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
