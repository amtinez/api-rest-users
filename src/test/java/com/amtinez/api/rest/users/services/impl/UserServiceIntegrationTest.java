package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.UserService;
import org.apache.commons.lang3.StringUtils;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@Transactional
public class UserServiceIntegrationTest {

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final LocalDate TEST_USER_DATE = LocalDate.now();

    private static final String TEST_ROLE_NAME = "testRoleName";

    @Resource
    private UserService userService;

    private UserModel userModel;

    @BeforeEach
    public void setUp() {
        userModel = userService.saveUser(UserModel.builder()
                                                  .firstName(TEST_USER_FIRST_NAME)
                                                  .lastName(TEST_USER_LAST_NAME)
                                                  .email(TEST_USER_EMAIL)
                                                  .password(TEST_USER_PASSWORD)
                                                  .birthdayDate(TEST_USER_DATE)
                                                  .lastAccessDate(TEST_USER_DATE)
                                                  .lastPasswordUpdateDate(TEST_USER_DATE)
                                                  .enabled(Boolean.TRUE)
                                                  .roles(Collections.singleton(RoleModel.builder()
                                                                                        .name(TEST_ROLE_NAME)
                                                                                        .build()))
                                                  .build());
    }

    @Test
    public void testFindUser() {
        final Optional<UserModel> userModelFound = userService.findUser(userModel.getId());
        assertTrue(userModelFound.isPresent());
        assertThat(userModelFound.get().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userModelFound.get().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    public void testFindUserNotExists() {
        final Optional<UserModel> userModelFound = userService.findUser(Long.MAX_VALUE);
        assertFalse(userModelFound.isPresent());
    }

    @Test
    public void testFindAllUsers() {
        final List<UserModel> users = userService.findAllUsers();
        assertFalse(users.isEmpty());
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void testSaveUser() {
        assertThat(userModel.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userModel.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(userModel.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userModel.getPassword()).isEqualTo(TEST_USER_PASSWORD);
        assertThat(userModel.getBirthdayDate()).isEqualTo(TEST_USER_DATE);
        assertThat(userModel.getLastAccessDate()).isEqualTo(TEST_USER_DATE);
        assertThat(userModel.getLastPasswordUpdateDate()).isEqualTo(TEST_USER_DATE);
        assertTrue(userModel.getEnabled());
        assertThat(userModel.getRoles().size()).isEqualTo(1);
        assertTrue(userModel.getRoles().stream().findFirst().isPresent());
        assertThat(userModel.getRoles().stream().findFirst().get().getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(userModel.getId());
        assertTrue(userService.findUser(userModel.getId()).isEmpty());
    }

    @Test
    public void testExistsUserEmail() {
        assertTrue(userService.existsUserEmail(TEST_USER_EMAIL));
    }

    @Test
    public void testExistsUserEmailUserNotExists() {
        assertFalse(userService.existsUserEmail(StringUtils.EMPTY));
    }

    @Test
    public void testUpdateUserEnabledStatus() {
        assertThat(userService.updateUserEnabledStatus(userModel.getId(), Boolean.FALSE)).isEqualTo(1);
    }

    @Test
    public void testUpdateUserEnabledStatusUserNotExists() {
        assertThat(userService.updateUserEnabledStatus(Long.MAX_VALUE, Boolean.FALSE)).isEqualTo(0);
    }

}
