package com.amtinez.api.rest.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Token {

    private Long id;
    private String code;
    private User user;

}
