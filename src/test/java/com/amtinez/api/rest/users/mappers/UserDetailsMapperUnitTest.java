package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserDetailsMapperUnitTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final LocalDate EXPIRED_LOCAL_DATE = LocalDate.of(2020, 1, 1);

    private static final String ROLE_NAME = "testName";

    @Mock
    private RoleModel roleModel;

    @Mock
    private UserModel userModel;

    private final UserDetailsMapper mapper = new UserDetailsMapperImpl();

    @BeforeEach
    public void setUp() {
        when(userModel.getId()).thenReturn(TEST_USER_ID);
        when(userModel.getFirstName()).thenReturn(TEST_USER_FIRST_NAME);
        when(userModel.getLastName()).thenReturn(TEST_USER_LAST_NAME);
        when(userModel.getPassword()).thenReturn(TEST_USER_PASSWORD);
        when(userModel.getEmail()).thenReturn(TEST_USER_EMAIL);
        when(userModel.getEnabled()).thenReturn(Boolean.TRUE);
        when(userModel.getLocked()).thenReturn(Boolean.FALSE);
        when(userModel.getLastAccessDate()).thenReturn(LocalDate.now());
        when(userModel.getLastPasswordUpdateDate()).thenReturn(LocalDate.now());

        when(roleModel.getName()).thenReturn(ROLE_NAME);
        when(userModel.getRoles()).thenReturn(Collections.singleton(roleModel));
    }

    @Test
    public void userModelToUserDetails() {
        final UserDetailsImpl userDetails = mapper.userModelToUserDetails(userModel);
        assertThat(userDetails.getId()).isEqualTo(TEST_USER_ID);
        assertThat(userDetails.getFirstName()).isEqualTo(TEST_USER_FIRST_NAME);
        assertThat(userDetails.getLastName()).isEqualTo(TEST_USER_LAST_NAME);
        assertThat(userDetails.getPassword()).isEqualTo(TEST_USER_PASSWORD);
        assertThat(userDetails.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertTrue(userDetails.isEnabled());
        assertThat(userDetails.getAuthorities().size()).isEqualTo(1);
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void userModelToUserDetailsExpired() {
        when(userModel.getLastAccessDate()).thenReturn(EXPIRED_LOCAL_DATE);
        when(userModel.getLastPasswordUpdateDate()).thenReturn(EXPIRED_LOCAL_DATE);
        final UserDetailsImpl userDetails = mapper.userModelToUserDetails(userModel);
        assertFalse(userDetails.isAccountNonExpired());
        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void userModelToUserDetailsNullExpiredDates() {
        when(userModel.getLastAccessDate()).thenReturn(null);
        when(userModel.getLastPasswordUpdateDate()).thenReturn(null);
        final UserDetailsImpl userDetails = mapper.userModelToUserDetails(userModel);
        assertFalse(userDetails.isAccountNonExpired());
        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void nullUserModelToUserDetails() {
        assertNull(mapper.userModelToUserDetails(null));
    }

}
