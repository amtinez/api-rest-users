package com.amtinez.api.rest.users.models;

import com.amtinez.api.rest.users.constants.DatabaseConstants.Table.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class TokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Token.TOKEN_FIELD, length = Token.TOKEN_FIELD_LENGTH, nullable = false)
    private String token;

    @Column(name = Token.CREATION_DATE_FIELD, nullable = false)
    private String creationDate;

    @Column(name = Token.EXPIRY_DATE_FIELD, nullable = false)
    private String expiryDate;

    @OneToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(name = Token.USER_ID_FIELD, nullable = false)
    private UserModel user;

}
