package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.amtinez.api.rest.users.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link UserMapperImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class UserMapperUnitTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final LocalDate TEST_USER_BIRTHDAY_DATE = LocalDate.now();
    private static final String TEST_USER_FIRST_NAME_UPDATED = "testUserFirstNameUpdated";

    private static final Long TEST_ROLE_ID = 1L;
    private static final String TEST_ROLE_NAME = "testRoleName";

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserMapperImpl mapper;

    private PasswordEncoder passwordEncoder;
    private UserModel userModel;
    private User user;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        final RoleModel roleModel = RoleModel.builder()
                                             .id(TEST_ROLE_ID)
                                             .name(TEST_ROLE_NAME)
                                             .build();

        userModel = UserModel.builder()
                             .id(TEST_USER_ID)
                             .firstName(TEST_USER_FIRST_NAME)
                             .lastName(TEST_USER_LAST_NAME)
                             .email(TEST_USER_EMAIL)
                             .birthdayDate(TEST_USER_BIRTHDAY_DATE)
                             .roles(Stream.of((roleModel))
                                          .collect(Collectors.toSet()))
                             .build();

        final Role role = Role.builder()
                              .id(TEST_ROLE_ID)
                              .name(TEST_ROLE_NAME)
                              .build();

        user = User.builder()
                   .id(TEST_USER_ID)
                   .firstName(TEST_USER_FIRST_NAME)
                   .lastName(TEST_USER_LAST_NAME)
                   .password(TEST_USER_PASSWORD)
                   .email(TEST_USER_EMAIL)
                   .birthdayDate(TEST_USER_BIRTHDAY_DATE)
                   .roles(Stream.of(role)
                                .collect(Collectors.toSet()))
                   .build();

        Mockito.when(roleService.findRole(USER.name())).thenReturn(Optional.of(roleModel));
    }

    @Test
    void modelToDto() {
        final User user = mapper.userModelToUser(userModel);
        assertThat(user.getId()).isEqualTo(TEST_USER_ID);
        assertThat(user.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(user.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertNull(user.getPassword());
        assertThat(user.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(user.getBirthdayDate()).isEqualTo(TEST_USER_BIRTHDAY_DATE);
        final Set<Role> roles = user.getRoles();
        assertThat(roles).hasSize(1);
        final Role role = roles.iterator().next();
        assertThat(role.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(role.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    void modelToDtoNullRoles() {
        userModel.setRoles(null);
        final User user = mapper.userModelToUser(userModel);
        assertNull(user.getRoles());
    }

    @Test
    void modelToDtoNullRole() {
        userModel.setRoles(Collections.singleton(null));
        final User user = mapper.userModelToUser(userModel);
        assertNull(user.getRoles().iterator().next());
    }

    @Test
    void nullModelToDto() {
        assertNull(mapper.userModelToUser(null));
    }

    @Test
    void dtoToModel() {
        final UserModel userModel = mapper.userToUserModel(user);
        assertThat(userModel.getId()).isEqualTo(TEST_USER_ID);
        assertThat(userModel.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userModel.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(userModel.getPassword()).isEqualTo(TEST_USER_PASSWORD);
        assertThat(userModel.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userModel.getBirthdayDate()).isEqualTo(TEST_USER_BIRTHDAY_DATE);
        final Set<RoleModel> roles = userModel.getRoles();
        assertThat(roles).hasSize(1);
        final RoleModel roleModel = roles.iterator().next();
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    void dtoToModelNullRoles() {
        user.setRoles(null);
        final UserModel userModel = mapper.userToUserModel(user);
        assertNull(userModel.getRoles());
    }

    @Test
    void dtoToModelNullRole() {
        user.setRoles(Collections.singleton(null));
        final UserModel userModel = mapper.userToUserModel(user);
        assertNull(userModel.getRoles().iterator().next());
    }

    @Test
    void nullDtoToModel() {
        assertNull(mapper.userToUserModel(null));
    }

    @Test
    void dtoToModelRegisterStep() {
        final UserModel userModel = mapper.userToUserModelRegisterStep(user, passwordEncoder, roleService);
        assertThat(userModel.getId()).isEqualTo(TEST_USER_ID);
        assertThat(userModel.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userModel.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(userModel.getPassword()).isNotEqualTo(TEST_USER_PASSWORD);
        assertThat(userModel.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userModel.getBirthdayDate()).isEqualTo(TEST_USER_BIRTHDAY_DATE);
        assertFalse(userModel.getEnabled());
        assertFalse(userModel.getLocked());
        final Set<RoleModel> roles = userModel.getRoles();
        assertThat(roles).hasSize(1);
        final RoleModel roleModel = roles.iterator().next();
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    void nullDtoToModelRegisterStep() {
        assertNull(mapper.userToUserModelRegisterStep(null, passwordEncoder, roleService));
    }

    @Test
    void dtoToModelRegisterStepNullPasswordEncoder() {
        UserModel userModel = mapper.userToUserModelRegisterStep(user, null, roleService);
        assertThat(userModel.getPassword()).isEqualTo(TEST_USER_PASSWORD);
    }

    @Test
    void dtoToModelRegisterStepRoleNotExists(final CapturedOutput output) {
        Mockito.when(roleService.findRole(USER.name())).thenReturn(Optional.empty());
        UserModel userModel = mapper.userToUserModelRegisterStep(user, passwordEncoder, roleService);
        assertThat(userModel.getRoles()).isEqualTo(Collections.emptySet());
        assertThat(output.getOut()).contains("The default role with name " + USER.name() + " not exists");
    }

    @Test
    void dtoToModelRegisterStepNullRoleService() {
        UserModel userModel = mapper.userToUserModelRegisterStep(user, passwordEncoder, null);
        assertThat(userModel.getRoles()).isEqualTo(Collections.emptySet());
    }

    @Test
    void dtoToModelUpdate() {
        user.setFirstName(TEST_USER_FIRST_NAME_UPDATED);
        user.setEnabled(Boolean.TRUE);
        user.setLocked(Boolean.TRUE);
        mapper.updateUserModelFromUser(userModel, user, passwordEncoder);
        assertThat(userModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(userModel.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME_UPDATED);
        assertThat(userModel.getPassword()).isNotEqualTo(TEST_USER_PASSWORD);
        assertTrue(userModel.getEnabled());
        assertTrue(userModel.getLocked());
    }

    @Test
    void dtoToModelUpdateNullRoles() {
        userModel.setRoles(null);
        mapper.updateUserModelFromUser(userModel, user, passwordEncoder);
        assertThat(userModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(userModel.getRoles()).hasSize(1);
    }

    @Test
    void nullDtoToModelUpdate() {
        assertNull(mapper.updateUserModelFromUser(userModel, null, passwordEncoder));
    }

    @Test
    void dtoToModelUpdateNullPasswordEncoder() {
        mapper.updateUserModelFromUser(userModel, user, null);
        assertThat(userModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(userModel.getPassword()).isEqualTo(TEST_USER_PASSWORD);
    }

}
