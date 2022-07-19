package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.common.BaseMailIntegrationTest;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.facades.RoleFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import static com.amtinez.api.rest.users.enums.Role.ADMIN;
import static com.amtinez.api.rest.users.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for {@link RoleFacadeImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleFacadeIntegrationTest extends BaseMailIntegrationTest {

    @Resource
    private RoleFacade roleFacade;

    private Role testRole;

    @BeforeAll
    public void setUp() {
        testRole = roleFacade.saveRole(Role.builder()
                                           .name(USER.name())
                                           .build());
    }

    @AfterAll
    public void cleanUp() {
        roleFacade.deleteRole(testRole.getId());
    }

    @Test
    void testFindRole() {
        final Optional<Role> roleFound = roleFacade.findRole(testRole.getId());
        assertTrue(roleFound.isPresent());
        assertThat(roleFound.get().getName()).isEqualTo(USER.name());
    }

    @Test
    void testFindRoleNotExists() {
        final Optional<Role> roleFound = roleFacade.findRole(Long.MAX_VALUE);
        assertFalse(roleFound.isPresent());
    }

    @Test
    void testFindAllRoles() {
        final List<Role> roles = roleFacade.findAllRoles();
        assertFalse(roles.isEmpty());
        assertThat(roles).hasSize(1);
    }

    @Test
    void testSaveRole() {
        final Role role = Role.builder()
                              .name(ADMIN.name())
                              .build();
        final Role roleSaved = roleFacade.saveRole(role);
        assertNotNull(roleSaved);
        assertThat(roleSaved.getName()).isEqualTo(ADMIN.name());
    }

    @Test
    void testUpdateRole() {
        final Role role = Role.builder()
                              .id(testRole.getId())
                              .name(ADMIN.name())
                              .build();
        final Optional<Role> roleUpdated = roleFacade.updateRole(role);
        assertTrue(roleUpdated.isPresent());
        assertThat(roleUpdated.get().getName()).isEqualTo(ADMIN.name());
    }

    @Test
    void testUpdateRoleNotFound() {
        final Role role = Role.builder()
                              .id(Long.MAX_VALUE)
                              .name(ADMIN.name())
                              .build();
        final Optional<Role> roleUpdated = roleFacade.updateRole(role);
        assertTrue(roleUpdated.isEmpty());
    }

    @Test
    void testDeleteRole() {
        roleFacade.deleteRole(testRole.getId());
        assertTrue(roleFacade.findRole(testRole.getId()).isEmpty());
    }

}
