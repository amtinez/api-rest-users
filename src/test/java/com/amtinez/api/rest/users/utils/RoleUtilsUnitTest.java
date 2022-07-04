package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.models.RoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit test for {@link RoleUtils}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
class RoleUtilsUnitTest {

    private static final String TEST_ROLE_NAME = "testRoleName";
    private static final String TEST_ROLE_NAME_UPPER_CASE_FORMATTED = "ROLE_TESTROLENAME";
    private static final String TEST_ROLE_NAME_UPPER_CASE_WITHOUT_PREFIX = "TESTROLENAME";

    private RoleModel roleModel;

    @BeforeEach
    public void setUp() {
        roleModel = RoleModel.builder()
                             .name(TEST_ROLE_NAME)
                             .build();
    }

    @Test
    void testGetPrefixedName() {
        assertThat(RoleUtils.getPrefixedName(roleModel)).isEqualTo(TEST_ROLE_NAME_UPPER_CASE_FORMATTED);
    }

    @Test
    void testGetNameWithoutPrefix() {
        assertThat(RoleUtils.getNameWithoutPrefix(TEST_ROLE_NAME_UPPER_CASE_FORMATTED)).isEqualTo(TEST_ROLE_NAME_UPPER_CASE_WITHOUT_PREFIX);
    }

}
