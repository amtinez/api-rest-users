package com.amtinez.api.rest.users.validations.validators;

import com.amtinez.api.rest.users.annotations.EqualPassword;
import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class EqualPasswordValidator implements ConstraintValidator<EqualPassword, PasswordResetToken> {

    /**
     * Returns whether the passwords are the same or not.
     *
     * @param passwordResetToken the password reset token
     * @param context            the constraint validator context
     * @return if the passwords are equal or not
     */
    @Override
    public boolean isValid(final PasswordResetToken passwordResetToken, final ConstraintValidatorContext context) {
        return StringUtils.equals(passwordResetToken.getPassword(), passwordResetToken.getRepeatedPassword());
    }

}
