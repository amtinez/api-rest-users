package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    UserModel findByEmail(final String email);

    /**
     * Retrieves if exists an user with the given email address
     *
     * @param email the email of the user
     * @return if email address exists
     */
    boolean existsByEmail(final String email);

    /**
     * Enables or disables the user if it exists
     *
     * @param id      the id of the user
     * @param enabled the enabled of the user
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
     * @param locked the locked of the user
     * @return number of updated users
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserModel AS U SET U.locked = :locked WHERE U.id = :id")
    int updateLockedStatusById(@Param("id") final Long id, @Param("locked") final Boolean locked);

}
