package com.amtinez.api.rest.users.daos;

import com.amtinez.api.rest.users.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface RoleDao extends JpaRepository<RoleModel, Long> {

    /**
     * Retrieves the role with the given name
     *
     * @param name the name of the role
     * @return the role if found
     */
    Optional<RoleModel> findByName(final String name);

}
