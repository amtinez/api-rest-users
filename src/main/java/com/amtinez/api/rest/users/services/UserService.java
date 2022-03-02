package com.amtinez.api.rest.users.services;

import com.amtinez.api.rest.users.models.UserModel;

import java.util.List;
import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface UserService {

    /**
     * Retrieves the user model with the given id
     *
     * @param id the id of the user model
     * @return the user model if found
     */
    Optional<UserModel> findUser(final Long id);

    /**
     * Retrieves the user model with the given email
     *
     * @param email the email of the user model
     * @return the user model if found
     */
    Optional<UserModel> findUser(final String email);

    /**
     * Retrieves the list of all user models
     *
     * @return the list of all user models
     */
    List<UserModel> findAllUsers();

    /**
     * Save the user model
     *
     * @param user the user model
     * @return the saved user model
     */
    UserModel saveUser(final UserModel user);

    /**
     * Delete the user model with the given id
     *
     * @param id the id of the user model
     */
    void deleteUser(final Long id);

    /**
     * Update the user password if user exists
     *
     * @param id       the id of the user
     * @param password the password of the user
     * @return number of updated users
     */
    int updateUserPassword(final Long id, final String password);

    /**
     * Enables or disables the user if it exists
     *
     * @param id      the id of the user
     * @param enabled the enabled of the user
     * @return number of updated users
     */
    int updateUserEnabledStatus(final Long id, final Boolean enabled);

    /**
     * Lock or unlock the user if it exists
     *
     * @param id     the id of the user
     * @param locked the locked of the user
     * @return number of updated users
     */
    int updateLockedStatusById(final Long id, final Boolean locked);

}
