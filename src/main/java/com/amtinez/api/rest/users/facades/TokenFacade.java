package com.amtinez.api.rest.users.facades;

import com.amtinez.api.rest.users.dtos.Token;
import com.amtinez.api.rest.users.models.TokenModel;
import com.amtinez.api.rest.users.services.TokenService;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface TokenFacade<T> {

    /**
     * Get the token if not expired
     *
     * @param code the code of the token
     * @return the token if not expired
     */
    Optional<Token> getUnexpiredToken(final String code);

    /**
     * Check if the token has expired. If expired, it is deleted.
     *
     * @param token the token
     * @return if the token has expired
     */
    default boolean isUnexpiredToken(final TokenService<T> tokenService, final T token) {
        return Optional.of(token)
                       .filter(TokenModel.class::isInstance)
                       .map(TokenModel.class::cast)
                       .map(tokenModelFound -> {
                           final boolean isUnexpired = LocalDate.now().isBefore(tokenModelFound.getExpiryDate());
                           if (!isUnexpired) {
                               tokenService.deleteToken(token);
                           }
                           return isUnexpired;
                       })
                       .orElse(Boolean.FALSE);
    }

}