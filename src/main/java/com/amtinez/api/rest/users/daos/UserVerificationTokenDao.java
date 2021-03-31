package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface UserVerificationTokenDao extends JpaRepository<UserVerificationTokenModel, Long> {

}
