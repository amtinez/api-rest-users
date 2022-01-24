package com.amtinez.api.rest.users.listeners;

import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link AuthenticationSuccessEventListener}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class AuthenticationSuccessEventListenerUnitTest {

    private static final Long TEST_USER_ID = 1L;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationSuccessEventListener authenticationSuccessEventListener;

    private AuthenticationSuccessEvent authenticationSuccessEvent;

    @BeforeEach
    public void setUp() {
        final UserDetailsImpl userDetails = UserDetailsImpl.builder()
                                                           .id(TEST_USER_ID)
                                                           .build();
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                                                                                      userDetails.getPassword(),
                                                                                      userDetails.getAuthorities());
        authenticationSuccessEvent = new AuthenticationSuccessEvent(authentication);
    }

    @Test
    public void testOnApplicationEvent() {
        final UserModel user = UserModel.builder()
                                        .id(TEST_USER_ID)
                                        .build();
        Mockito.when(userService.findUser(TEST_USER_ID)).thenReturn(Optional.of(user));
        authenticationSuccessEventListener.onApplicationEvent(authenticationSuccessEvent);
        verify(userService).findUser(TEST_USER_ID);
        assertThat(user.getLastAccessDate()).isEqualTo(LocalDate.now());
        verify(userService).saveUser(user);
    }

    @Test
    public void testOnApplicationEventUserLoggedNotFound(final CapturedOutput output) {
        authenticationSuccessEventListener.onApplicationEvent(authenticationSuccessEvent);
        verify(userService).findUser(TEST_USER_ID);
        verify(userService, Mockito.never()).saveUser(Mockito.any(UserModel.class));
        assertThat(output.getOut()).contains("User logged with id " + TEST_USER_ID + " not found");
    }

}
