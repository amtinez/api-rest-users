package com.amtinez.api.rest.users.listeners;

import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

    @Resource
    private UserService userService;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {
        Optional.of(event)
                .map(AbstractAuthenticationEvent::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast)
                .ifPresent(userDetails -> {
                    final Optional<UserModel> userFound = userService.findUser(userDetails.getId());
                    userFound.ifPresentOrElse(
                        userLogged -> {
                            userLogged.setLastAccessDate(LocalDateTime.now());
                            userService.saveUser(userLogged);
                        },
                        () -> LOG.error("User logged with id {} not found", userDetails.getId())
                    );
                });
    }

}
