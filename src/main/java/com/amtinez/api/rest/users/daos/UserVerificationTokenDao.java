package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface UserVerificationTokenDao extends JpaRepository<UserVerificationTokenModel, Long> {

    /**
     * Retrieves the user verification token with the given code
     *
     * @param code the code of the user verification token
     * @return the user verification token if found
     */
    Optional<UserVerificationTokenModel> findByCode(final String code);

    /**
     * Retrieves the user verification token with the given user
     *
     * @param userModel the user of the user verification token
     * @return the user verification token if found
     */
    Optional<UserVerificationTokenModel> findByUser(final UserModel userModel);

    /**
     * Deletes the user verification token with the given user id
     *
     * @param id the user id of the user verification token
     */
    @Transactional
    void deleteByUserId(final Long id);

}
