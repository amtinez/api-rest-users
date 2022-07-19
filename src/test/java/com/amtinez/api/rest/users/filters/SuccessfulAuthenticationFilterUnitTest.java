package com.amtinez.api.rest.users.filters;

import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Unit test for {@link SuccessfulAuthenticationFilter}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SuccessfulAuthenticationFilterUnitTest {

    private static final Long TEST_USER_ID = 1L;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private SuccessfulAuthenticationFilter authenticationFilter;

    @BeforeEach
    public void setUp() {
        Mockito.when(userDetails.getId()).thenReturn(TEST_USER_ID);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    void onSuccessfulAuthentication() throws IOException {
        authenticationFilter.onSuccessfulAuthentication(request, response, authentication);
        Mockito.verify(userService).updateUserLastAccess(TEST_USER_ID);
    }

}
