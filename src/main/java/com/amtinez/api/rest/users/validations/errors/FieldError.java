package com.amtinez.api.rest.users.validations.errors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Builder
@Getter
@Setter
public class FieldError {

    private String field;
    private String message;

}
