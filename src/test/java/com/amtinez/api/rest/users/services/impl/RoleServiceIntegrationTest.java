package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.services.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for {@link RoleServiceImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleServiceIntegrationTest {

    private static final String TEST_ROLE_NAME = "testRoleName";

    @Resource
    private RoleService roleService;

    private RoleModel testRole;

    @BeforeAll
    public void setUp() {
        testRole = roleService.saveRole(RoleModel.builder()
                                                 .name(TEST_ROLE_NAME)
                                                 .build());
    }

    @AfterAll
    public void cleanUp() {
        roleService.deleteRole(testRole.getId());
    }

    @Test
    void testFindRole() {
        final Optional<RoleModel> roleFound = roleService.findRole(testRole.getId());
        assertTrue(roleFound.isPresent());
        assertThat(roleFound.get().getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    void testFindRoleNotExists() {
        final Optional<RoleModel> roleFound = roleService.findRole(Long.MAX_VALUE);
        assertFalse(roleFound.isPresent());
    }

    @Test
    void testFindRoleByName() {
        final Optional<RoleModel> roleFound = roleService.findRole(testRole.getName());
        assertTrue(roleFound.isPresent());
        assertThat(roleFound.get().getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    void testFindRoleByNameNotExists() {
        final Optional<RoleModel> roleFound = roleService.findRole(StringUtils.EMPTY);
        assertFalse(roleFound.isPresent());
    }

    @Test
    void testFindAllRoles() {
        final List<RoleModel> roles = roleService.findAllRoles();
        assertFalse(roles.isEmpty());
        assertThat(roles).hasSize(1);
    }

    @Test
    void testSaveRole() {
        assertNotNull(testRole);
        assertNotNull(testRole.getId());
        assertThat(testRole.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    void testDeleteRole() {
        roleService.deleteRole(testRole.getId());
        assertTrue(roleService.findRole(testRole.getId()).isEmpty());
    }

}
