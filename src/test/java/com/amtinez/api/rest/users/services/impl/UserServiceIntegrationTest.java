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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private static final String TEST_ROLE_NAME = "testRoleName";

    @Resource
    private UserService userService;

    private UserModel testUser;

    @BeforeEach
    public void setUp() {
        testUser = userService.saveUser(UserModel.builder()
                                                 .firstName(TEST_USER_FIRST_NAME)
                                                 .lastName(TEST_USER_LAST_NAME)
                                                 .email(TEST_USER_EMAIL)
                                                 .password(TEST_USER_PASSWORD)
                                                 .birthdayDate(LocalDate.now())
                                                 .lastAccessDate(LocalDate.now())
                                                 .lastPasswordUpdateDate(LocalDate.now())
                                                 .enabled(Boolean.TRUE)
                                                 .locked(Boolean.FALSE)
                                                 .roles(Collections.singleton(RoleModel.builder()
                                                                                       .name(TEST_ROLE_NAME)
                                                                                       .build()))
                                                 .build());
    }

    @Test
    public void testFindUser() {
        final Optional<UserModel> userFound = userService.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertThat(userFound.get().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userFound.get().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    public void testFindUserNotExists() {
        final Optional<UserModel> userFound = userService.findUser(Long.MAX_VALUE);
        assertFalse(userFound.isPresent());
    }

    @Test
    public void testFindAllUsers() {
        final List<UserModel> users = userService.findAllUsers();
        assertFalse(users.isEmpty());
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void testSaveUser() {
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
        assertThat(testUser.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(testUser.getPassword()).isEqualTo(TEST_USER_PASSWORD);
        assertThat(testUser.getBirthdayDate()).isEqualTo(LocalDate.now());
        assertThat(testUser.getLastAccessDate()).isEqualTo(LocalDate.now());
        assertThat(testUser.getLastPasswordUpdateDate()).isEqualTo(LocalDate.now());
        assertTrue(testUser.getEnabled());
        assertThat(testUser.getRoles().size()).isEqualTo(1);
        assertTrue(testUser.getRoles().stream().findFirst().isPresent());
        assertThat(testUser.getRoles().stream().findFirst().get().getName()).isEqualTo(TEST_ROLE_NAME);
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(testUser.getId());
        assertTrue(userService.findUser(testUser.getId()).isEmpty());
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
    public void testUpdateUserEnabledStatusEnable() {
        assertThat(userService.updateUserEnabledStatus(testUser.getId(), Boolean.TRUE)).isEqualTo(1);
        final Optional<UserModel> userFound = userService.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertTrue(userFound.get().getEnabled());
    }

    @Test
    public void testUpdateUserEnabledStatusDisable() {
        assertThat(userService.updateUserEnabledStatus(testUser.getId(), Boolean.FALSE)).isEqualTo(1);
        final Optional<UserModel> userFound = userService.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertFalse(userFound.get().getEnabled());
    }

    @Test
    public void testUpdateUserEnabledStatusUserNotExists() {
        assertThat(userService.updateUserEnabledStatus(Long.MAX_VALUE, Boolean.FALSE)).isZero();
    }

    @Test
    public void testUpdateLockedStatusByIdLocked() {
        assertThat(userService.updateLockedStatusById(testUser.getId(), Boolean.TRUE)).isEqualTo(1);
        final Optional<UserModel> userFound = userService.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertTrue(userFound.get().getLocked());
    }

    @Test
    public void testUpdateLockedStatusByIdUnlocked() {
        assertThat(userService.updateLockedStatusById(testUser.getId(), Boolean.FALSE)).isEqualTo(1);
        final Optional<UserModel> userFound = userService.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertFalse(userFound.get().getLocked());
    }

    @Test
    public void testUpdateLockedStatusByIdUserNotExists() {
        assertThat(userService.updateUserEnabledStatus(Long.MAX_VALUE, Boolean.FALSE)).isZero();
    }

}
