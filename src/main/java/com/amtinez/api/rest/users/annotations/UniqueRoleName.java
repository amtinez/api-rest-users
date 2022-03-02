package com.amtinez.api.rest.users.annotations;

import com.amtinez.api.rest.users.validations.validators.UniqueRoleNameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * UniqueRoleName is the interface used to implement the uniqueness restriction of a role name.
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueRoleNameValidator.class)
public @interface UniqueRoleName {

    String message() default "a role with that name already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
