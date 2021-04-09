package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserMapperUnitTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final LocalDate TEST_USER_BIRTHDAY_DATE = LocalDate.now();

    private static final Long TEST_ROLE_ID = 1L;
    private static final String TEST_ROLE_NAME = "testRoleName";

    @Mock
    private UserModel userModel;

    @Mock
    private RoleModel roleModel;

    @Mock
    private User user;

    @Mock
    private Role role;

    private final UserMapper mapper = new UserMapperImpl();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        when(userModel.getId()).thenReturn(TEST_USER_ID);
        when(userModel.getFirstName()).thenReturn(TEST_USER_FIRST_NAME);
        when(userModel.getLastName()).thenReturn(TEST_USER_LAST_NAME);
        when(userModel.getEmail()).thenReturn(TEST_USER_EMAIL);
        when(userModel.getBirthdayDate()).thenReturn(TEST_USER_BIRTHDAY_DATE);

        when(roleModel.getId()).thenReturn(TEST_ROLE_ID);
        when(roleModel.getName()).thenReturn(TEST_ROLE_NAME);
        when(userModel.getRoles()).thenReturn(Collections.singleton(roleModel));

        when(user.getId()).thenReturn(TEST_USER_ID);
        when(user.getFirstName()).thenReturn(TEST_USER_FIRST_NAME);
        when(user.getLastName()).thenReturn(TEST_USER_LAST_NAME);
        when(user.getPassword()).thenReturn(TEST_USER_PASSWORD);
        when(user.getEmail()).thenReturn(TEST_USER_EMAIL);
        when(user.getBirthdayDate()).thenReturn(TEST_USER_BIRTHDAY_DATE);
        when(role.getId()).thenReturn(TEST_ROLE_ID);
        when(role.getName()).thenReturn(TEST_ROLE_NAME);
        when(user.getRoles()).thenReturn(Collections.singleton(role));
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
        assertThat(roles.size()).isEqualTo(1);
        final Role role = roles.iterator().next();
        assertThat(role.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(role.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void modelToDtoNullRoles() {
        when(userModel.getRoles()).thenReturn(null);
        final User user = mapper.userModelToUser(userModel);
        assertNull(user.getRoles());
    }

    @Test
    public void modelToDtoNullRole() {
        when(userModel.getRoles()).thenReturn(Collections.singleton(null));
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
        assertThat(roles.size()).isEqualTo(1);
        final RoleModel roleModel = roles.iterator().next();
        assertThat(roleModel.getId()).isEqualTo(TEST_ROLE_ID);
        assertThat(roleModel.getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void dtoToModelNullRoles() {
        when(user.getRoles()).thenReturn(null);
        final UserModel userModel = mapper.userToUserModel(user);
        assertNull(userModel.getRoles());
    }

    @Test
    public void dtoToModelNullRole() {
        when(user.getRoles()).thenReturn(Collections.singleton(null));
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
        assertThat(roles.size()).isEqualTo(1);
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

}
