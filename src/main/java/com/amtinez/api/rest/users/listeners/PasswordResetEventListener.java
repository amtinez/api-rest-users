package com.amtinez.api.rest.users.listeners;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.events.PasswordResetEvent;
import com.amtinez.api.rest.users.mappers.PasswordResetTokenMapper;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
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
public class PasswordResetEventListener implements ApplicationListener<PasswordResetEvent> {

    private static final String EMAIL_SUBJECT = "Reset password";
    private static final String PASSWORD_RESET_MESSAGE = "Please visit the following URL to reset your password";

    @Resource
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;

    @Resource
    private EmailService emailService;

    @Resource
    private PasswordResetTokenMapper passwordResetTokenMapper;

    @Override
    public void onApplicationEvent(final PasswordResetEvent event) {
        Optional.of(event.getSource())
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .ifPresent(user -> {
                    final PasswordResetTokenModel token = passwordResetTokenMapper.userToTokenModel(user);
                    passwordResetTokenService.saveToken(token);
                    final String appBaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                    emailService.sendEmail(user.getEmail(),
                                           EMAIL_SUBJECT,
                                           PASSWORD_RESET_MESSAGE
                                               + StringUtils.SPACE
                                               + appBaseUrl
                                               + "/users/reset/"
                                               + token.getCode());
                });
    }

}
