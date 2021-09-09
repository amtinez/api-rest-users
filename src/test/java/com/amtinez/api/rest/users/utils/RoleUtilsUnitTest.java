package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.models.RoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
public class RoleUtilsUnitTest {

    private static final String TEST_ROLE_NAME = "testRoleName";
    private static final String TEST_ROLE_NAME_UPPER_CASE_FORMATTED = "ROLE_TESTROLENAME";

    private RoleModel roleModel;

    @BeforeEach
    public void setUp() {
        roleModel = RoleModel.builder()
                             .name(TEST_ROLE_NAME)
                             .build();
    }

    @Test
    public void testGetPrefixedName() {
        assertThat(RoleUtils.getPrefixedName(roleModel)).isEqualTo(TEST_ROLE_NAME_UPPER_CASE_FORMATTED);
    }

}
