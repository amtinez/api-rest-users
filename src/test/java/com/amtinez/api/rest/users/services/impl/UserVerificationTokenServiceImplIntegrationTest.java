package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.services.TokenService;
import com.amtinez.api.rest.users.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
 * Integration test for {@link UserVerificationTokenServiceImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserVerificationTokenServiceImplIntegrationTest {

    private static final String TEST_CODE = "testCode";

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";

    @Resource
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;
    @Resource
    private UserService userService;

    private UserModel testUser;
    private UserVerificationTokenModel testUserVerificationToken;

    @BeforeAll
    public void setUp() {
        testUser = userService.saveUser(UserModel.builder()
                                                 .firstName(TEST_USER_FIRST_NAME)
                                                 .lastName(TEST_USER_LAST_NAME)
                                                 .email(TEST_USER_EMAIL)
                                                 .password(TEST_USER_PASSWORD)
                                                 .birthdayDate(LocalDate.now())
                                                 .build());
        testUserVerificationToken = userVerificationTokenService.saveToken(UserVerificationTokenModel.builder()
                                                                                                     .code(TEST_CODE)
                                                                                                     .creationDate(LocalDate.now())
                                                                                                     .expiryDate(LocalDate.now())
                                                                                                     .user(testUser)
                                                                                                     .build());
    }

    @AfterAll
    public void cleanUp() {
        userVerificationTokenService.deleteToken(testUserVerificationToken);
        userService.deleteUser(testUser.getId());
    }

    @Test
    void testFindTokenById() {
        final Optional<UserVerificationTokenModel> tokenFound = userVerificationTokenService.findToken(testUserVerificationToken.getId());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_CODE);
    }

    @Test
    void testFindTokenByCode() {
        final Optional<UserVerificationTokenModel> tokenFound = userVerificationTokenService.findToken(testUserVerificationToken.getCode());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_CODE);
    }

    @Test
    void testFindTokenByUser() {
        final Optional<UserVerificationTokenModel> tokenFound = userVerificationTokenService.findToken(testUserVerificationToken.getUser());
        assertTrue(tokenFound.isPresent());
        assertThat(tokenFound.get().getCode()).isEqualTo(TEST_CODE);
    }

    @Test
    void testSaveToken() {
        assertNotNull(testUserVerificationToken);
        assertNotNull(testUserVerificationToken.getId());
        assertThat(testUserVerificationToken.getCode()).isEqualTo(TEST_CODE);
        assertThat(testUserVerificationToken.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(testUserVerificationToken.getExpiryDate()).isEqualTo(LocalDate.now());
        assertNotNull(testUserVerificationToken.getUser());
        assertThat(testUserVerificationToken.getUser().getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(testUserVerificationToken.getUser().getLastName()).isEqualTo(TEST_USER_LAST_NAME);
    }

    @Test
    void testDeleteToken() {
        userVerificationTokenService.deleteToken(testUserVerificationToken.getId());
        assertTrue(userVerificationTokenService.findToken(testUserVerificationToken.getId()).isEmpty());
    }

    @Test
    void testDeleteTokenByToken() {
        userVerificationTokenService.deleteToken(testUserVerificationToken);
        assertTrue(userVerificationTokenService.findToken(testUserVerificationToken.getId()).isEmpty());
    }

    @Test
    void testDeleteTokenByUserId() {
        userVerificationTokenService.deleteTokenByUserId(testUser.getId());
        assertTrue(userVerificationTokenService.findToken(testUserVerificationToken.getId()).isEmpty());
    }

}
