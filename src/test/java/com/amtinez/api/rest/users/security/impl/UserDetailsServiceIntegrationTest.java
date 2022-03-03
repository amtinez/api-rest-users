package com.amtinez.api.rest.users.security.impl;

import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import javax.annotation.Resource;

import static com.amtinez.api.rest.users.enums.Role.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test for {@link UserDetailsService}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
class UserDetailsServiceIntegrationTest {

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_EMAIL_NOT_EXISTS = "test@user.notexists";
    private static final String TEST_USER_PASSWORD = "testUserPassword";

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private UserService userService;

    @Test
    void testLoadUserByUsernameExists() {
        userService.saveUser(UserModel.builder()
                                      .firstName(TEST_USER_FIRST_NAME)
                                      .lastName(TEST_USER_LAST_NAME)
                                      .email(TEST_USER_EMAIL)
                                      .password(TEST_USER_PASSWORD)
                                      .birthdayDate(LocalDate.now())
                                      .lastAccessDate(LocalDate.now())
                                      .lastPasswordUpdateDate(LocalDate.now())
                                      .enabled(Boolean.TRUE)
                                      .roles(Collections.singleton(RoleModel.builder()
                                                                            .name(USER.name())
                                                                            .build()))
                                      .build());
        assertNotNull(userDetailsService.loadUserByUsername(TEST_USER_EMAIL));
    }

    @Test
    void testLoadUserByUsernameNotExists() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(TEST_USER_EMAIL_NOT_EXISTS));
    }

}
