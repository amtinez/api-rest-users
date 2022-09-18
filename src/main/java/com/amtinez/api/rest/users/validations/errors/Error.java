package com.amtinez.api.rest.users.validations.errors;

import com.amtinez.api.rest.users.views.View;
import com.fasterxml.jackson.annotation.JsonView;
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
@JsonView(value = View.Anonymous.class)
public class Error {

    private List<ValidationError> errors;

}
