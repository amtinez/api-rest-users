package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.constants.ConfigurationConstants;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.amtinez.api.rest.users.constants.SecurityConstants.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for {@link SecurityUtils}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@ActiveProfiles(ConfigurationConstants.Profiles.TEST)
public class SecurityUtilsIntegrationTest {

    private static final Long TEST_LOGGED_IN_USER_ID = 1L;

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testIsAdminUser() {
        assertTrue(SecurityUtils.isAdminUser());
    }

    @Test
    @WithMockUser
    public void testIsNotAdminUser() {
        assertFalse(SecurityUtils.isAdminUser());
    }

    @Test
    @WithMockUser
    public void testIsUserSameLoggedInUser() {
        setTestLoggedInUserId();
        assertTrue(SecurityUtils.isUserSameLoggedInUser(User.builder()
                                                            .id(TEST_LOGGED_IN_USER_ID)
                                                            .build()));
    }

    @Test
    @WithMockUser
    public void testIsNotUserSameLoggedInUser() {
        assertFalse(SecurityUtils.isUserSameLoggedInUser(User.builder()
                                                             .id(TEST_LOGGED_IN_USER_ID)
                                                             .build()));
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testCanLoggedInUserUpdateThisUserAdminUser() {
        assertTrue(SecurityUtils.canLoggedInUserUpdateThisUser(User.builder()
                                                                   .build()));
    }

    @Test
    @WithMockUser
    public void testCanLoggedInUserUpdateThisUserNormalUser() {
        setTestLoggedInUserId();
        assertTrue(SecurityUtils.canLoggedInUserUpdateThisUser(User.builder()
                                                                   .id(TEST_LOGGED_IN_USER_ID)
                                                                   .build()));
    }

    @Test
    @WithMockUser
    public void testCanLoggedInUserUpdateThisUserOtherUser() {
        assertFalse(SecurityUtils.canLoggedInUserUpdateThisUser(User.builder()
                                                                    .id(TEST_LOGGED_IN_USER_ID)
                                                                    .build()));
    }

    private void setTestLoggedInUserId() {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast)
                .ifPresent(userDetails -> userDetails.setId(TEST_LOGGED_IN_USER_ID));
    }

}
