package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link UserMapperImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class UserMapperUnitTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final LocalDate TEST_USER_BIRTHDAY_DATE = LocalDate.now();
    private static final String TEST_USER_FIRST_NAME_UPDATED = "testUserFirstNameUpdated";

    private static final Long TEST_ROLE_ID = 1L;
    private static final String TEST_ROLE_NAME = "testRoleName";

    private UserModel userModel;
    private User user;

    private final UserMapper mapper = new UserMapperImpl();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    public void modelToDto() {
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
    public void modelToDtoNullRoles() {
        userModel.setRoles(null);
        final User user = mapper.userModelToUser(userModel);
        assertNull(user.getRoles());
    }

    @Test
    public void modelToDtoNullRole() {
        userModel.setRoles(Collections.singleton(null));
        final User user = mapper.userModelToUser(userModel);
        assertNull(user.getRoles().iterator().next());
    }

    @Test
    public void nullModelToDto() {
        assertNull(mapper.userModelToUser(null));
    }

    @Test
    public void dtoToModel() {
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
    public void dtoToModelNullRoles() {
        user.setRoles(null);
        final UserModel userModel = mapper.userToUserModel(user);
        assertNull(userModel.getRoles());
    }

    @Test
    public void dtoToModelNullRole() {
        user.setRoles(Collections.singleton(null));
        final UserModel userModel = mapper.userToUserModel(user);
        assertNull(userModel.getRoles().iterator().next());
    }

    @Test
    public void nullDtoToModel() {
        assertNull(mapper.userToUserModel(null));
    }

    @Test
    public void dtoToModelRegisterStep() {
        final UserModel userModel = mapper.userToUserModelRegisterStep(user, passwordEncoder);
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
    public void nullDtoToModelRegisterStep() {
        assertNull(mapper.userToUserModelRegisterStep(null, passwordEncoder));
    }

    @Test
    public void dtoToModelRegisterStepNullPasswordEncoder() {
        UserModel userModel = mapper.userToUserModelRegisterStep(user, null);
        assertThat(userModel.getPassword()).isEqualTo(TEST_USER_PASSWORD);
    }

    @Test
    public void dtoToModelUpdate() {
        user.setFirstName(TEST_USER_FIRST_NAME_UPDATED);
        user.setEnabled(Boolean.TRUE);
        user.setLocked(Boolean.TRUE);
        mapper.updateUserModelFromUser(userModel, user);
        assertThat(userModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(userModel.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME_UPDATED);
        assertTrue(userModel.getEnabled());
        assertTrue(userModel.getLocked());
    }

    @Test
    public void dtoToModelUpdateNullRoles() {
        userModel.setRoles(null);
        mapper.updateUserModelFromUser(userModel, user);
        assertThat(userModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(userModel.getRoles()).hasSize(1);
    }

    @Test
    public void nullDtoToModelUpdate() {
        assertNull(mapper.updateUserModelFromUser(userModel, null));
    }

}
