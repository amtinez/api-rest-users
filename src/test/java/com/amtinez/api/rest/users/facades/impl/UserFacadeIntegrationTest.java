package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.constants.ConfigurationConstants;
import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.dtos.UserVerificationToken;
import com.amtinez.api.rest.users.facades.UserFacade;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import static com.amtinez.api.rest.users.constants.SecurityConstants.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for {@link UserFacadeImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@ActiveProfiles(ConfigurationConstants.Profiles.TEST)
@ExtendWith(OutputCaptureExtension.class)
@Transactional
class UserFacadeIntegrationTest {

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_NEW_PASSWORD = "testUserNewPassword";

    private static final String TEST_ROLE_NAME = "testRoleName";

    private static final String TEST_USER_FIRST_NAME_UPDATED = "testUserFirstNameUpdated";

    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Resource
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;
    @Resource
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;
    @Resource
    private UserFacade userFacade;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = userFacade.registerUser(User.builder()
                                               .firstName(TEST_USER_FIRST_NAME)
                                               .lastName(TEST_USER_LAST_NAME)
                                               .email(TEST_USER_EMAIL)
                                               .password(TEST_USER_PASSWORD)
                                               .birthdayDate(LocalDate.now())
                                               .roles(Collections.singleton(Role.builder()
                                                                                .name(TEST_ROLE_NAME)
                                                                                .build()))
                                               .build());
    }

    @Test
    void testFindUser() {
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertThat(userFound.get().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userFound.get().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testGetCurrentUser() {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast)
                .ifPresent(userDetails -> userDetails.setId(testUser.getId()));
        final Optional<User> userFound = userFacade.getCurrentUser();
        assertTrue(userFound.isPresent());
        assertThat(userFound.get().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userFound.get().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    void testGetCurrentUserNotExists() {
        final Optional<User> userFound = userFacade.getCurrentUser();
        assertTrue(userFound.isEmpty());
    }

    @Test
    void testDeleteUser() {
        userFacade.deleteUser(testUser.getId());
        assertTrue(userFacade.findUser(testUser.getId()).isEmpty());
    }

    @Test
    void testFindAllUsers() {
        final List<User> users = userFacade.findAllUsers();
        assertFalse(users.isEmpty());
        assertThat(users).hasSize(1);
    }

    @Test
    void testRegisterUser() {
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
        assertThat(testUser.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertNull(testUser.getPassword());
        assertThat(testUser.getBirthdayDate()).isEqualTo(LocalDate.now());
        assertFalse(testUser.getEnabled());
        assertThat(testUser.getRoles()).hasSize(1);
        assertTrue(userVerificationTokenService.findToken(UserModel.builder()
                                                                   .id(testUser.getId())
                                                                   .build())
                                               .isPresent());
    }

    @Test
    void testConfirmRegisterUser() {
        userFacade.confirmRegisterUser(UserVerificationToken.builder()
                                                            .user(testUser)
                                                            .build());
        final Optional<User> testUserRegistered = userFacade.findUser(testUser.getId());
        assertTrue(testUserRegistered.isPresent());
        assertTrue(testUserRegistered.get().getEnabled());
    }

    @Test
    void testConfirmRegisterUserNotExists(final CapturedOutput output) {
        userFacade.confirmRegisterUser(UserVerificationToken.builder()
                                                            .code(TEST_TOKEN_CODE)
                                                            .build());
        assertThat(output.getOut()).contains("Token with code " + TEST_TOKEN_CODE + " has no user");
    }

    @Test
    void testSendUserPasswordResetEmail() {
        userFacade.sendUserPasswordResetEmail(testUser);
        assertTrue(passwordResetTokenService.findToken(UserModel.builder()
                                                                .id(testUser.getId())
                                                                .build())
                                            .isPresent());
    }

    @Test
    void testUpdatePasswordUser() {
        userFacade.updatePasswordUser(PasswordResetToken.builder()
                                                        .password(TEST_USER_NEW_PASSWORD)
                                                        .user(testUser)
                                                        .build());
        final Optional<User> testUserRegistered = userFacade.findUser(testUser.getId());
        assertTrue(testUserRegistered.isPresent());
        assertNull(testUser.getPassword());
    }

    @Test
    void testUpdatePasswordUserUserNotExists(final CapturedOutput output) {
        userFacade.updatePasswordUser(PasswordResetToken.builder()
                                                        .code(TEST_TOKEN_CODE)
                                                        .build());
        assertThat(output.getOut()).contains("Token with code " + TEST_TOKEN_CODE + " has no user");
    }

    @Test
    void testEnableUser() {
        final int affectedUsers = userFacade.enableUser(testUser.getId());
        assertThat(affectedUsers).isOne();
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertTrue(userFound.get().getEnabled());
    }

    @Test
    void testEnableUserNotExists() {
        final int affectedUsers = userFacade.enableUser(Long.MAX_VALUE);
        assertThat(affectedUsers).isZero();
    }

    @Test
    void testDisableUser() {
        final int affectedUsers = userFacade.disableUser(testUser.getId());
        assertThat(affectedUsers).isOne();
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertFalse(userFound.get().getEnabled());
    }

    @Test
    void testDisableUserNotExists() {
        final int affectedUsers = userFacade.disableUser(Long.MAX_VALUE);
        assertThat(affectedUsers).isZero();
    }

    @Test
    void testUpdateUser() {
        final User user = User.builder()
                              .id(testUser.getId())
                              .firstName(TEST_USER_FIRST_NAME_UPDATED)
                              .build();
        final Optional<User> userUpdated = userFacade.updateUser(user);
        assertTrue(userUpdated.isPresent());
        assertThat(userUpdated.get().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME_UPDATED);
        assertThat(userUpdated.get().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(userUpdated.get().getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userUpdated.get().getRoles()).hasSize(1);
    }

    @Test
    void testLockUser() {
        final int affectedUsers = userFacade.lockUser(testUser.getId());
        assertThat(affectedUsers).isOne();
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertTrue(userFound.get().getLocked());
    }

    @Test
    void testLockUserNotExists() {
        final int affectedUsers = userFacade.lockUser(Long.MAX_VALUE);
        assertThat(affectedUsers).isZero();
    }

    @Test
    void testUnlockUser() {
        final int affectedUsers = userFacade.unlockUser(testUser.getId());
        assertThat(affectedUsers).isOne();
        final Optional<User> userFound = userFacade.findUser(testUser.getId());
        assertTrue(userFound.isPresent());
        assertFalse(userFound.get().getLocked());
    }

    @Test
    void testUnlockUserNotExists() {
        final int affectedUsers = userFacade.unlockUser(Long.MAX_VALUE);
        assertThat(affectedUsers).isZero();
    }

}
