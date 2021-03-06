package com.amtinez.api.rest.users.facades;

import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.dtos.UserVerificationToken;

import java.util.List;
import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface UserFacade {

    /**
     * Retrieves the user with the given id
     *
     * @param id the id of the user
     * @return the user if found
     */
    Optional<User> findUser(final Long id);

    /**
     * Retrieves the current user
     *
     * @return the current user if found
     */
    Optional<User> getCurrentUser();

    /**
     * Retrieves the list of all users
     *
     * @return the list of all users
     */
    List<User> findAllUsers();

    /**
     * Register the user
     *
     * @param user the user
     * @return the registered user
     */
    User registerUser(final User user);

    /**
     * Confirm register the user
     *
     * @param userVerificationToken the user verification token
     */
    void confirmRegisterUser(final UserVerificationToken userVerificationToken);

    /**
     * Sends the user password reset e-mail
     *
     * @param user the user
     */
    void sendUserPasswordResetEmail(final User user);

    /**
     * Updates the user password
     *
     * @param passwordResetToken the password reset token
     */
    void updatePasswordUser(final PasswordResetToken passwordResetToken);

    /**
     * Enables the user
     *
     * @param id the id of the user
     * @return number of updated users
     */
    int enableUser(final Long id);

    /**
     * Disables the user
     *
     * @param id the id of the user
     * @return number of updated users
     */
    int disableUser(final Long id);

    /**
     * Delete the user with the given id
     *
     * @param id the id of the user
     */
    void deleteUser(final Long id);

    /**
     * Update the user
     *
     * @param user the user
     * @return the updated user if found
     */
    Optional<User> updateUser(final User user);

    /**
     * Locks the user
     *
     * @param id the id of the user
     * @return number of updated users
     */
    int lockUser(final Long id);

    /**
     * Unlocks the user
     *
     * @param id the id of the user
     * @return number of updated users
     */
    int unlockUser(final Long id);

}
