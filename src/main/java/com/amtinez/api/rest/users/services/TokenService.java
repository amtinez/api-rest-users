package com.amtinez.api.rest.users.services;

import com.amtinez.api.rest.users.models.UserModel;

import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface TokenService<T> {

    /**
     * Retrieves the token model with the given id
     *
     * @param id the id of the token model
     * @return the token model if found
     */
    Optional<T> findToken(final Long id);

    /**
     * Retrieves the token with the given code
     *
     * @param code the code of the token
     * @return the token if found
     */
    Optional<T> findToken(final String code);

    /**
     * Retrieves the token with the given user
     *
     * @param user the user of the token
     * @return the token if found
     */
    Optional<T> findToken(final UserModel user);

    /**
     * Save the token model
     *
     * @param token the token model
     * @return the saved token model
     */
    T saveToken(final T token);

    /**
     * Delete the token model with the given id
     *
     * @param id the id of the token model
     */
    void deleteToken(final Long id);

}
