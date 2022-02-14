package com.amtinez.api.rest.users.listeners;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.events.RegistrationSuccessEvent;
import com.amtinez.api.rest.users.mappers.UserVerificationTokenModelMapper;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.services.EmailService;
import com.amtinez.api.rest.users.services.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Component
public class RegistrationSuccessEventListener implements ApplicationListener<RegistrationSuccessEvent> {

    private static final String EMAIL_SUBJECT = "Registration Confirmation";
    private static final String REGISTRATION_CONFIRMATION_MESSAGE = "Please visit the following URL to confirm your registration";

    @Resource
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;

    @Resource
    private EmailService emailService;

    @Resource
    private UserVerificationTokenModelMapper userVerificationTokenModelMapper;

    @Override
    public void onApplicationEvent(final RegistrationSuccessEvent event) {
        Optional.of(event.getSource())
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .ifPresent(user -> {
                    final UserVerificationTokenModel token = userVerificationTokenModelMapper.userToUserVerificationTokenModel(user);
                    userVerificationTokenService.saveToken(token);
                    final String appBaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                    emailService.sendEmail(user.getEmail(),
                                           EMAIL_SUBJECT,
                                           REGISTRATION_CONFIRMATION_MESSAGE
                                               + StringUtils.SPACE
                                               + appBaseUrl
                                               + "/users/confirm/"
                                               + token.getCode());
                });
    }

}
