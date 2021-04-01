package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.facades.RoleFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@Transactional
public class RoleFacadeIntegrationTest {

    private static final String TEST_ROLE_NAME = "testName";

    @Resource
    private RoleFacade roleFacade;

    private Role testRole;

    @BeforeEach
    public void setUp() {
        testRole = roleFacade.saveRole(Role.builder()
                                           .name(TEST_ROLE_NAME)
                                           .build());
    }

    @Test
    public void testFindRole() {
        final Optional<Role> roleFound = roleFacade.findRole(testRole.getId());
        assertTrue(roleFound.isPresent());
        assertThat(roleFound.get().getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void testFindRoleNotExists() {
        final Optional<Role> roleFound = roleFacade.findRole(Long.MAX_VALUE);
        assertFalse(roleFound.isPresent());
    }

    @Test
    public void testFindAllRoles() {
        final List<Role> roles = roleFacade.findAllRoles();
        assertFalse(roles.isEmpty());
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    public void testSaveRole() {
        final Role role = Role.builder()
                              .name(TEST_ROLE_NAME)
                              .build();
        final Role roleSaved = roleFacade.saveRole(role);
        assertNotNull(roleSaved);
        assertThat(roleSaved.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void testDeleteRole() {
        roleFacade.deleteRole(testRole.getId());
        assertTrue(roleFacade.findRole(testRole.getId()).isEmpty());
    }

}
