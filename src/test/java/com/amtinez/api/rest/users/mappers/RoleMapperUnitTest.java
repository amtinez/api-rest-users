package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.models.RoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
public class RoleMapperUnitTest {

    private static final Long TEST_ROLE_ID = 1L;
    private static final String TEST_ROLE_NAME = "testName";

    @Mock
    private RoleModel roleModel;

    @Mock
    private Role role;

    private final RoleMapper mapper = new RoleMapperImpl();

    @BeforeEach
    public void setUp() {
        lenient().when(roleModel.getId()).thenReturn(TEST_ROLE_ID);
        lenient().when(roleModel.getName()).thenReturn(TEST_ROLE_NAME);

        lenient().when(role.getId()).thenReturn(TEST_ROLE_ID);
        lenient().when(role.getName()).thenReturn(TEST_ROLE_NAME);
    }

    @Test
    public void modelToDto() {
        final Role role = mapper.roleModelToRole(roleModel);
        assertThat(role.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(role.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void nullModelToDto() {
        assertNull(mapper.roleModelToRole(null));
    }

    @Test
    public void dtoToModel() {
        final RoleModel roleModel = mapper.roleToRoleModel(role);
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void nullDtoToModel() {
        assertNull(mapper.roleToRoleModel(null));
    }

}
