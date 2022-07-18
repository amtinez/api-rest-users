package com.amtinez.api.rest.users.dtos;

import com.amtinez.api.rest.users.validations.constraints.UniqueUserEmail;
import com.amtinez.api.rest.users.validations.groups.PasswordResetEmail;
import com.amtinez.api.rest.users.validations.groups.Update;
import com.amtinez.api.rest.users.views.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
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
import javax.validation.groups.Default;

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

    @NotNull(groups = Update.class)
    @JsonView(value = View.User.class)
    private Long id;

    @NotBlank
    @Size(max = FIRST_NAME_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    @JsonView(value = View.User.class)
    private String firstName;

    @NotBlank
    @Size(max = LAST_NAME_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    @JsonView(value = View.User.class)
    private String lastName;

    @Email
    @NotBlank
    @UniqueUserEmail(groups = {Default.class, Update.class})
    @Size(max = EMAIL_MAX_FIELD_LENGTH, groups = {Default.class, Update.class, PasswordResetEmail.class})
    @JsonView(value = View.User.class)
    private String email;

    @NotBlank
    @Size(max = PASSWORD_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    private String password;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonView(value = View.User.class)
    private LocalDate birthdayDate;

    @JsonView(value = View.Admin.class)
    private Boolean enabled;

    @JsonView(value = View.Admin.class)
    private Boolean locked;

    @JsonView(value = View.Admin.class)
    private Set<Role> roles;

}
