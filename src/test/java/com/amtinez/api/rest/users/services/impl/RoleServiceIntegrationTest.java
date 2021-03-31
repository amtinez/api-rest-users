package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.services.RoleService;
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
public class RoleServiceIntegrationTest {

    private static final String TEST_ROLE_NAME = "testRoleName";

    @Resource
    private RoleService roleService;

    private RoleModel testRole;

    @BeforeEach
    public void setUp() {
        testRole = roleService.saveRole(RoleModel.builder()
                                                 .name(TEST_ROLE_NAME)
                                                 .build());
    }

    @Test
    public void testFindRole() {
        final Optional<RoleModel> roleFound = roleService.findRole(testRole.getId());
        assertTrue(roleFound.isPresent());
        assertThat(roleFound.get().getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void testFindRoleNotExists() {
        final Optional<RoleModel> roleFound = roleService.findRole(Long.MAX_VALUE);
        assertFalse(roleFound.isPresent());
    }

    @Test
    public void testFindAllRoles() {
        final List<RoleModel> roles = roleService.findAllRoles();
        assertFalse(roles.isEmpty());
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    public void testSaveRole() {
        assertNotNull(testRole);
        assertNotNull(testRole.getId());
        assertThat(testRole.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void testDeleteRole() {
        roleService.deleteRole(testRole.getId());
        assertTrue(roleService.findRole(testRole.getId()).isEmpty());
    }

}
