package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.facades.RoleFacade;
import com.amtinez.api.rest.users.facades.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@ActiveProfiles(ConfigurationConstants.Profiles.TEST)
@Transactional
public class UserFacadeIntegrationTest {

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";

    private static final String TEST_ROLE_NAME = "testRoleName";

    private static final String TEST_USER_FIRST_NAME_UPDATED = "testUserFirstNameUpdated";

    @Resource
    private UserFacade userFacade;

    @Resource
    private RoleFacade roleFacade;

    private User testUser;

    @BeforeEach
    public void setUp() {
        final Role testRole = roleFacade.saveRole(Role.builder()
                                                      .name(TEST_ROLE_NAME)
                                                      .build());
        testUser = userFacade.registerUser(User.builder()
                                               .firstName(TEST_USER_FIRST_NAME)
                                               .lastName(TEST_USER_LAST_NAME)
                                               .email(TEST_USER_EMAIL)
                                               .password(TEST_USER_PASSWORD)
                                               .birthdayDate(LocalDate.now())
                                               .roles(Collections.singleton(Role.builder()
                                                                                .id(testRole.getId())
                                                                                .build()))
                                               .build());
    }

    @Test
    public void testFindUser() {
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertThat(userFound.get().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userFound.get().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    public void testDeleteUser() {
        userFacade.deleteUser(testUser.getId());
        assertTrue(userFacade.findUser(testUser.getId()).isEmpty());
    }

    @Test
    public void testFindAllUsers() {
        final List<User> users = userFacade.findAllUsers();
        assertFalse(users.isEmpty());
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void testRegisterUser() {
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
        assertThat(testUser.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertNull(testUser.getPassword());
        assertThat(testUser.getBirthdayDate()).isEqualTo(LocalDate.now());
        assertFalse(testUser.getEnabled());
        assertThat(testUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testEnableUser() {
        final int affectedUsers = userFacade.enableUser(testUser.getId());
        assertThat(affectedUsers).isEqualTo(1);
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertTrue(userFound.get().getEnabled());
    }

    @Test
    public void testEnableUserNotExists() {
        final int affectedUsers = userFacade.enableUser(Long.MAX_VALUE);
        assertThat(affectedUsers).isZero();
    }

    @Test
    public void testDisableUser() {
        final int affectedUsers = userFacade.disableUser(testUser.getId());
        assertThat(affectedUsers).isEqualTo(1);
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertFalse(userFound.get().getEnabled());
    }

    @Test
    public void testDisableUserNotExists() {
        final int affectedUsers = userFacade.disableUser(Long.MAX_VALUE);
        assertThat(affectedUsers).isZero();
    }

    @Test
    public void testUpdateUser() {
        final User user = User.builder()
                              .id(testUser.getId())
                              .firstName(TEST_USER_FIRST_NAME_UPDATED)
                              .build();
        final User userUpdated = userFacade.updateUser(user);
        assertThat(userUpdated.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME_UPDATED);
    }

}
