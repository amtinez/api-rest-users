package com.amtinez.api.rest.users.annotations;

import com.amtinez.api.rest.users.validations.validators.EqualPasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * EqualPassword is the interface used to implement the equality constraint when updating the user's password.
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EqualPasswordValidator.class)
public @interface EqualPassword {

    String message() default "passwords are not equal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
