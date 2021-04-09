package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.models.RoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoleUtilsUnitTest {

    private static final String TEST_ROLE_NAME = "testRoleName";
    private static final String TEST_ROLE_NAME_UPPER_CASE_FORMATTED = "ROLE_TESTROLENAME";

    @Mock
    private RoleModel roleModel;

    @BeforeEach
    public void setUp() {
        when(roleModel.getName()).thenReturn(TEST_ROLE_NAME);
    }

    @Test
    public void testGetPrefixedName() {
        assertThat(TEST_ROLE_NAME_UPPER_CASE_FORMATTED).isEqualTo(RoleUtils.getPrefixedName(roleModel));
    }

}
