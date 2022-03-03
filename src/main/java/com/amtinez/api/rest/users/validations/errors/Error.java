package com.amtinez.api.rest.users.validations.errors;

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
public class Error {

    private List<FieldError> errors;

}
