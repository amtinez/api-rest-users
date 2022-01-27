package com.amtinez.api.rest.users.dtos;

import com.amtinez.api.rest.users.annotations.UniqueUserEmail;
import com.amtinez.api.rest.users.validations.groups.Update;
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
    private Long id;

    @NotBlank
    @Size(max = FIRST_NAME_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    private String firstName;

    @NotBlank
    @Size(max = LAST_NAME_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    private String lastName;

    @Email
    @NotBlank
    @Size(max = EMAIL_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    @UniqueUserEmail(groups = {Update.class})
    private String email;

    @NotBlank
    @Size(max = PASSWORD_MAX_FIELD_LENGTH, groups = {Default.class, Update.class})
    private String password;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthdayDate;

    private Boolean enabled;

    private Boolean locked;

    private Set<Role> roles;

}
