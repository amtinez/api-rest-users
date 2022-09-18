package com.amtinez.api.rest.users.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UserVerificationToken extends AbstractToken {

}
