package com.amtinez.api.rest.users.validations.validators;

import com.amtinez.api.rest.users.annotations.UniqueUserEmail;
import com.amtinez.api.rest.users.services.UserService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    @Resource
    private UserService userService;

    /**
     * Returns whether the user's email exists or not
     *
     * @param email   the user's email
     * @param context the constraint validator context
     * @return if the user's email is valid
     */
    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        return userService.findUser(email).isEmpty();
    }

}
