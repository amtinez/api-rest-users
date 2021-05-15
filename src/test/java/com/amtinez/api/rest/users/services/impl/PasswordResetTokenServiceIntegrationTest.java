package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.TokenService;
import com.amtinez.api.rest.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@Transactional
public class PasswordResetTokenServiceIntegrationTest {

    private static final String TEST_TOKEN_CODE = "testTokenCode";

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";

    @Resource
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;
    @Resource
    private UserService userService;

    private PasswordResetTokenModel testPasswordResetToken;

    @BeforeEach
    public void setUp() {
        final UserModel userModel = userService.saveUser(UserModel.builder()
                                                                  .firstName(TEST_USER_FIRST_NAME)
                                                                  .lastName(TEST_USER_LAST_NAME)
                                                                  .email(TEST_USER_EMAIL)
                                                                  .password(TEST_USER_PASSWORD)
                                                                  .birthdayDate(LocalDate.now())
                                                                  .build());
        testPasswordResetToken = passwordResetTokenService.saveToken(PasswordResetTokenModel.builder()
                                                                                            .code(TEST_TOKEN_CODE)
                                                                                            .creationDate(LocalDate.now())
                                                                                            .expiryDate(LocalDate.now())
                                                                                            .user(userModel)
                                                                                            .build());
    }

    @Test
    public void testFindTokenById() {
        final Optional<PasswordResetTokenModel> tokenFound = passwordResetTokenService.findToken(testPasswordResetToken.getId());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_TOKEN_CODE);
    }

    @Test
    public void testFindTokenByToken() {
        final Optional<PasswordResetTokenModel> tokenFound = passwordResetTokenService.findToken(testPasswordResetToken.getCode());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_TOKEN_CODE);
    }

    @Test
    public void testFindTokenByUser() {
        final Optional<PasswordResetTokenModel> tokenFound = passwordResetTokenService.findToken(testPasswordResetToken.getUser());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_TOKEN_CODE);
    }

    @Test
    public void testSaveToken() {
        assertNotNull(testPasswordResetToken);
        assertNotNull(testPasswordResetToken.getId());
        assertThat(testPasswordResetToken.getCode()).isEqualTo(TEST_TOKEN_CODE);
        assertThat(testPasswordResetToken.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(testPasswordResetToken.getExpiryDate()).isEqualTo(LocalDate.now());
        assertNotNull(testPasswordResetToken.getUser());
        assertThat(testPasswordResetToken.getUser().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(testPasswordResetToken.getUser().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    public void testDeleteToken() {
        passwordResetTokenService.deleteToken(testPasswordResetToken.getId());
        assertTrue(passwordResetTokenService.findToken(testPasswordResetToken.getId()).isEmpty());
    }

}
