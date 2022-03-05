package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface PasswordResetTokenDao extends JpaRepository<PasswordResetTokenModel, Long> {

    /**
     * Retrieves the password reset token with the given code
     *
     * @param code the code of the password reset token
     * @return the password reset token if found
     */
    Optional<PasswordResetTokenModel> findByCode(final String code);

    /**
     * Retrieves the password reset token with the given user
     *
     * @param userModel the user of the password reset token
     * @return the password reset token if found
     */
    Optional<PasswordResetTokenModel> findByUser(final UserModel userModel);

    /**
     * Deletes the password reset token with the given user id
     *
     * @param id the user id of the password reset token
     */
    @Transactional
    void deleteByUserId(final Long id);

}
