package com.amtinez.api.rest.users.errors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Builder
@Getter
@Setter
public class ValidationError {

    private List<FieldError> errors;

}
