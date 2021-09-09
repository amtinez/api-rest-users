package com.amtinez.api.rest.users.dtos;

import com.amtinez.api.rest.users.validations.groups.Update;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.amtinez.api.rest.users.constants.ValidationConstants.Role.NAME_MAX_FIELD_LENGTH;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank
    @Size(max = NAME_MAX_FIELD_LENGTH)
    private String name;

}
