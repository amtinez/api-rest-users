package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.models.RoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amtinez.api.rest.users.enums.Role.ADMIN;
import static com.amtinez.api.rest.users.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for {@link RoleMapperImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
class RoleMapperUnitTest {

    private static final Long TEST_ROLE_ID = 1L;

    private final RoleMapper mapper = new RoleMapperImpl();

    private RoleModel roleModel;
    private Role role;

    @BeforeEach
    public void setUp() {
        roleModel = RoleModel.builder()
                             .id(TEST_ROLE_ID)
                             .name(USER.name())
                             .build();

        role = Role.builder()
                   .id(TEST_ROLE_ID)
                   .name(USER.name())
                   .build();
    }

    @Test
    void modelToDto() {
        final Role role = mapper.roleModelToRole(roleModel);
        assertThat(role.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(role.getName()).isEqualTo(USER.name());
    }

    @Test
    void nullModelToDto() {
        assertNull(mapper.roleModelToRole(null));
    }

    @Test
    void dtoToModel() {
        final RoleModel roleModel = mapper.roleToRoleModel(role);
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(USER.name());
    }

    @Test
    void nullDtoToModel() {
        assertNull(mapper.roleToRoleModel(null));
    }

    @Test
    void dtoToModelUpdate() {
        role.setName(ADMIN.name());
        mapper.updateRoleModelFromRole(roleModel, role);
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(ADMIN.name());
    }

    @Test
    void nullDtoToModelUpdate() {
        assertThat(mapper.updateRoleModelFromRole(roleModel, null)).isEqualTo(roleModel);
    }

}
