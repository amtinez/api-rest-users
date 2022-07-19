package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.TokenService;
import com.amtinez.api.rest.users.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for {@link PasswordResetTokenServiceImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PasswordResetTokenServiceIntegrationTest {

    private static final String TEST_TOKEN_CODE = "testTokenCode";

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";

    @Resource
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;
    @Resource
    private UserService userService;

    private UserModel testUser;
    private PasswordResetTokenModel testPasswordResetToken;

    @BeforeAll
    public void setUp() {
        testUser = userService.saveUser(UserModel.builder()
                                                 .firstName(TEST_USER_FIRST_NAME)
                                                 .lastName(TEST_USER_LAST_NAME)
                                                 .email(TEST_USER_EMAIL)
                                                 .password(TEST_USER_PASSWORD)
                                                 .birthdayDate(LocalDate.now())
                                                 .enabled(Boolean.TRUE)
                                                 .locked(Boolean.FALSE)
                                                 .build());
        testPasswordResetToken = passwordResetTokenService.saveToken(PasswordResetTokenModel.builder()
                                                                                            .code(TEST_TOKEN_CODE)
                                                                                            .creationDate(LocalDateTime.now())
                                                                                            .expiryDate(LocalDateTime.now())
                                                                                            .user(testUser)
                                                                                            .build());
    }

    @AfterAll
    public void cleanUp() {
        passwordResetTokenService.deleteToken(testPasswordResetToken);
        userService.deleteUser(testUser.getId());
    }

    @Test
    void testFindTokenById() {
        final Optional<PasswordResetTokenModel> tokenFound = passwordResetTokenService.findToken(testPasswordResetToken.getId());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_TOKEN_CODE);
    }

    @Test
    void testFindTokenByToken() {
        final Optional<PasswordResetTokenModel> tokenFound = passwordResetTokenService.findToken(testPasswordResetToken.getCode());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_TOKEN_CODE);
    }

    @Test
    void testFindTokenByUser() {
        final Optional<PasswordResetTokenModel> tokenFound = passwordResetTokenService.findToken(testPasswordResetToken.getUser());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_TOKEN_CODE);
    }

    @Test
    void testSaveToken() {
        assertNotNull(testPasswordResetToken);
        assertNotNull(testPasswordResetToken.getId());
        assertThat(testPasswordResetToken.getCode()).isEqualTo(TEST_TOKEN_CODE);
        assertThat(testPasswordResetToken.getCreationDate().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(testPasswordResetToken.getExpiryDate().toLocalDate()).isEqualTo(LocalDate.now());
        assertNotNull(testPasswordResetToken.getUser());
        assertThat(testPasswordResetToken.getUser().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(testPasswordResetToken.getUser().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    void testDeleteToken() {
        passwordResetTokenService.deleteToken(testPasswordResetToken.getId());
        assertTrue(passwordResetTokenService.findToken(testPasswordResetToken.getId()).isEmpty());
    }

    @Test
    void testDeleteTokenByToken() {
        passwordResetTokenService.deleteToken(testPasswordResetToken);
        assertTrue(passwordResetTokenService.findToken(testPasswordResetToken.getId()).isEmpty());
    }

    @Test
    void testDeleteTokenByUserId() {
        passwordResetTokenService.deleteTokenByUserId(testUser.getId());
        assertTrue(passwordResetTokenService.findToken(testPasswordResetToken.getId()).isEmpty());
    }

}
