package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface PasswordResetTokenDao extends JpaRepository<PasswordResetTokenModel, Long> {

}
