package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface UserDao extends JpaRepository<UserModel, Long> {

    /**
     * Retrieves the user with the given email address
     *
     * @param email the email of the user
     * @return the user if found
     */
    Optional<UserModel> findByEmail(final String email);

    /**
     * Update the user password if user exists
     *
     * @param id       the id of the user
     * @param password the password of the user
     * @param date     the date of the update
     * @return number of updated users
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserModel AS U SET U.password = :password, U.lastPasswordUpdateDate = :date WHERE U.id = :id")
    int updatePasswordById(@Param("id") final Long id, @Param("password") final String password, @Param("date") final LocalDateTime date);

    /**
     * Update the user last access if user exists
     *
     * @param id   the id of the user
     * @param date the date of the update
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserModel AS U SET U.lastAccessDate = :date WHERE U.id = :id")
    void updateLastAccessById(@Param("id") final Long id, @Param("date") final LocalDateTime date);

    /**
     * Enables or disables the user if it exists
     *
     * @param id      the id of the user
     * @param enabled if the user is enabled
     * @return number of updated users
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserModel AS U SET U.enabled = :enabled WHERE U.id = :id")
    int updateEnabledStatusById(@Param("id") final Long id, @Param("enabled") final Boolean enabled);

    /**
     * Lock or unlock the user if it exists
     *
     * @param id     the id of the user
     * @param locked if the user is locked
     * @return number of updated users
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserModel AS U SET U.locked = :locked WHERE U.id = :id")
    int updateLockedStatusById(@Param("id") final Long id, @Param("locked") final Boolean locked);

}
