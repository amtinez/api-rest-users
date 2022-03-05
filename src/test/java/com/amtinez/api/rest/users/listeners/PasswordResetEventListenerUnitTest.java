package com.amtinez.api.rest.users.listeners;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.events.PasswordResetEvent;
import com.amtinez.api.rest.users.mappers.PasswordResetTokenMapper;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
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
 * Unit test for {@link PasswordResetEventListener}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
class PasswordResetEventListenerUnitTest {

    private static final String EMAIL_SUBJECT = "Reset password";
    private static final String PASSWORD_RESET_MESSAGE = "Please visit the following URL to reset your password";

    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Mock
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordResetTokenMapper passwordResetTokenMapper;

    @Captor
    private ArgumentCaptor<String> userEmailArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> emailSubjectArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> emailMessageArgumentCaptor;

    @InjectMocks
    private PasswordResetEventListener passwordResetEventListener;

    private PasswordResetEvent passwordResetEvent;
    private User testUser;
    private PasswordResetTokenModel passwordResetTokenModel;

    @BeforeEach
    public void setUp() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        passwordResetTokenModel = PasswordResetTokenModel.builder()
                                                         .code(TEST_TOKEN_CODE)
                                                         .build();
        testUser = User.builder()
                       .email(TEST_USER_EMAIL)
                       .build();
        passwordResetEvent = new PasswordResetEvent(testUser);
    }

    @Test
    void testOnApplicationEvent() {
        Mockito.when(passwordResetTokenMapper.userToTokenModel(testUser))
               .thenReturn(passwordResetTokenModel);
        passwordResetEventListener.onApplicationEvent(passwordResetEvent);
        Mockito.verify(passwordResetTokenService)
               .saveToken(passwordResetTokenModel);
        Mockito.verify(emailService)
               .sendEmail(userEmailArgumentCaptor.capture(), emailSubjectArgumentCaptor.capture(), emailMessageArgumentCaptor.capture());
        assertThat(userEmailArgumentCaptor.getValue()).isEqualTo(TEST_USER_EMAIL);
        assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(EMAIL_SUBJECT);
        assertThat(emailMessageArgumentCaptor.getValue()).isEqualTo(PASSWORD_RESET_MESSAGE
                                                                        + StringUtils.SPACE
                                                                        + ServletUriComponentsBuilder.fromCurrentContextPath()
                                                                                                     .build()
                                                                                                     .toUriString()
                                                                        + "/users/reset/"
                                                                        + passwordResetTokenModel.getCode());
    }

}
