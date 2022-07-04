package com.amtinez.api.rest.users.dtos;

import com.amtinez.api.rest.users.validations.constraints.EqualPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.amtinez.api.rest.users.constants.ValidationConstants.User.PASSWORD_MAX_FIELD_LENGTH;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@EqualPassword
public class PasswordResetToken extends Token {

    @NotBlank
    @Size(max = PASSWORD_MAX_FIELD_LENGTH)
    private String password;

    @NotBlank
    @Size(max = PASSWORD_MAX_FIELD_LENGTH)
    private String repeatedPassword;

}
