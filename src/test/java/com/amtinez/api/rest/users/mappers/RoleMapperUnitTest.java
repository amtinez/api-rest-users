package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.models.RoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for {@link RoleMapperImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class RoleMapperUnitTest {

    private static final Long TEST_ROLE_ID = 1L;
    private static final String TEST_ROLE_NAME = "testName";
    private static final String TEST_ROLE_UPDATED_NAME = "testNameUpdated";

    private RoleModel roleModel;
    private Role role;

    private final RoleMapper mapper = new RoleMapperImpl();

    @BeforeEach
    public void setUp() {
        roleModel = RoleModel.builder()
                             .id(TEST_ROLE_ID)
                             .name(TEST_ROLE_NAME)
                             .build();

        role = Role.builder()
                   .id(TEST_ROLE_ID)
                   .name(TEST_ROLE_NAME)
                   .build();
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

    @Test
    public void dtoToModelUpdate() {
        role.setName(TEST_ROLE_UPDATED_NAME);
        mapper.updateRoleModelFromRole(roleModel, role);
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(TEST_ROLE_UPDATED_NAME);
    }

    @Test
    public void nullDtoToModelUpdate() {
        assertNull(mapper.updateRoleModelFromRole(roleModel, null));
    }

}
