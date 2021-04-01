package com.amtinez.api.rest.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.amtinez.api.rest.users.constants.ValidationConstants.User.EMAIL_MAX_FIELD_LENGTH;
import static com.amtinez.api.rest.users.constants.ValidationConstants.User.FIRST_NAME_MAX_FIELD_LENGTH;
import static com.amtinez.api.rest.users.constants.ValidationConstants.User.LAST_NAME_MAX_FIELD_LENGTH;
import static com.amtinez.api.rest.users.constants.ValidationConstants.User.PASSWORD_MAX_FIELD_LENGTH;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class User {

    private Long id;

    @NotBlank
    @Size(max = FIRST_NAME_MAX_FIELD_LENGTH)
    private String firstName;

    @NotBlank
    @Size(max = LAST_NAME_MAX_FIELD_LENGTH)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = EMAIL_MAX_FIELD_LENGTH)
    private String email;

    @NotBlank
    @Size(max = PASSWORD_MAX_FIELD_LENGTH)
    private String password;

    @NotNull
    private LocalDate birthdayDate;

    private Boolean enabled;

    private Set<Role> roles;

}
