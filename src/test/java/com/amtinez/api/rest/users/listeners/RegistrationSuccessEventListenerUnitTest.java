package com.amtinez.api.rest.users.listeners;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.events.RegistrationSuccessEvent;
import com.amtinez.api.rest.users.mappers.UserVerificationTokenModelMapper;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.services.EmailService;
import com.amtinez.api.rest.users.services.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link RegistrationSuccessEventListener}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
public class RegistrationSuccessEventListenerUnitTest {

    private static final String EMAIL_SUBJECT = "Registration Confirmation";
    private static final String REGISTRATION_CONFIRMATION_MESSAGE = "Please visit the following URL to confirm your registration";

    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Mock
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private UserVerificationTokenModelMapper userVerificationTokenModelMapper;

    @Captor
    private ArgumentCaptor<String> userEmailArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> emailSubjectArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> emailMessageArgumentCaptor;

    @InjectMocks
    private RegistrationSuccessEventListener registrationSuccessEventListener;

    private RegistrationSuccessEvent registrationSuccessEvent;
    private User testUser;
    private UserVerificationTokenModel userVerificationTokenModel;

    @BeforeEach
    public void setUp() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        userVerificationTokenModel = UserVerificationTokenModel.builder()
                                                               .code(TEST_TOKEN_CODE)
                                                               .build();
        testUser = User.builder()
                       .email(TEST_USER_EMAIL)
                       .build();
        registrationSuccessEvent = new RegistrationSuccessEvent(testUser);
    }

    @Test
    public void testOnApplicationEvent() {
        Mockito.when(userVerificationTokenModelMapper.userToUserVerificationTokenModel(testUser))
               .thenReturn(userVerificationTokenModel);
        registrationSuccessEventListener.onApplicationEvent(registrationSuccessEvent);
        Mockito.verify(userVerificationTokenService)
               .saveToken(userVerificationTokenModel);
        Mockito.verify(emailService)
               .sendEmail(userEmailArgumentCaptor.capture(), emailSubjectArgumentCaptor.capture(), emailMessageArgumentCaptor.capture());
        assertThat(userEmailArgumentCaptor.getValue()).isEqualTo(TEST_USER_EMAIL);
        assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(EMAIL_SUBJECT);
        assertThat(emailMessageArgumentCaptor.getValue()).isEqualTo(REGISTRATION_CONFIRMATION_MESSAGE
                                                                        + StringUtils.SPACE
                                                                        + ServletUriComponentsBuilder.fromCurrentContextPath()
                                                                                                     .build()
                                                                                                     .toUriString()
                                                                        + "/users/confirm/"
                                                                        + userVerificationTokenModel.getCode());
    }

}
