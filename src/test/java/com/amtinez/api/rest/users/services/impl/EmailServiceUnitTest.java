package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.configurations.properties.MailProperties;
import com.amtinez.api.rest.users.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link EmailService}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
public class EmailServiceUnitTest {

    private static final String TEST_MAIL_FROM = "test@from.com";
    private static final String TEST_MAIL_ADDRESS = "test@address.com";
    private static final String TEST_MAIL_SUBJECT = "testSubject";
    private static final String TEST_MAIL_MESSAGE = "testMessage";

    @Mock
    private MailProperties mailProperties;
    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    public void setUp() {
        Mockito.when(mailProperties.getFrom()).thenReturn(TEST_MAIL_FROM);
    }

    @Test
    public void testSendEmail() {
        emailService.sendEmail(TEST_MAIL_ADDRESS, TEST_MAIL_SUBJECT, TEST_MAIL_MESSAGE);
        Mockito.verify(mailSender).send(simpleMailMessageArgumentCaptor.capture());
        assertThat(simpleMailMessageArgumentCaptor.getValue().getFrom()).isEqualTo(TEST_MAIL_FROM);
        assertThat(simpleMailMessageArgumentCaptor.getValue().getTo()).isEqualTo(new String[]{TEST_MAIL_ADDRESS});
        assertThat(simpleMailMessageArgumentCaptor.getValue().getSubject()).isEqualTo(TEST_MAIL_SUBJECT);
        assertThat(simpleMailMessageArgumentCaptor.getValue().getText()).isEqualTo(TEST_MAIL_MESSAGE);
    }

}
