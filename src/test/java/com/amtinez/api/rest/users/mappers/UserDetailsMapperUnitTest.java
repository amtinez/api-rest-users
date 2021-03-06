package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.amtinez.api.rest.users.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link UserDetailsMapperImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
class UserDetailsMapperUnitTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final LocalDateTime EXPIRED_LOCAL_DATE = LocalDateTime.of(2020, 1, 1, 0, 0);

    private final UserDetailsMapper mapper = new UserDetailsMapperImpl();

    private UserModel userModel;

    @BeforeEach
    public void setUp() {
        final RoleModel roleModel = RoleModel.builder()
                                             .name(USER.name())
                                             .build();

        userModel = UserModel.builder()
                             .id(TEST_USER_ID)
                             .firstName(TEST_USER_FIRST_NAME)
                             .lastName(TEST_USER_LAST_NAME)
                             .password(TEST_USER_PASSWORD)
                             .email(TEST_USER_EMAIL)
                             .enabled(Boolean.TRUE)
                             .locked(Boolean.FALSE)
                             .lastAccessDate(LocalDateTime.now())
                             .lastPasswordUpdateDate(LocalDateTime.now())
                             .roles(Collections.singleton(roleModel))
                             .build();
    }

    @Test
    void userModelToUserDetails() {
        final UserDetailsImpl userDetails = mapper.userModelToUserDetails(userModel);
        assertThat(userDetails.getId()).isEqualTo(TEST_USER_ID);
        assertThat(userDetails.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userDetails.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(userDetails.getPassword()).isEqualTo(TEST_USER_PASSWORD);
        assertThat(userDetails.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertTrue(userDetails.isEnabled());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void userModelToUserDetailsExpired() {
        userModel.setLastAccessDate(EXPIRED_LOCAL_DATE);
        userModel.setLastPasswordUpdateDate(EXPIRED_LOCAL_DATE);
        final UserDetailsImpl userDetails = mapper.userModelToUserDetails(userModel);
        assertFalse(userDetails.isAccountNonExpired());
        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    void userModelToUserDetailsNullExpiredDates() {
        userModel.setLastAccessDate(null);
        userModel.setLastPasswordUpdateDate(null);
        final UserDetailsImpl userDetails = mapper.userModelToUserDetails(userModel);
        assertFalse(userDetails.isAccountNonExpired());
        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    void nullUserModelToUserDetails() {
        assertNull(mapper.userModelToUserDetails(null));
    }

}
