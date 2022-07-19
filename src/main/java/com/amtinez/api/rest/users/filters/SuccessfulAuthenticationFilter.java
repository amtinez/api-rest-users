package com.amtinez.api.rest.users.filters;

import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class SuccessfulAuthenticationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public SuccessfulAuthenticationFilter(final AuthenticationManager authenticationManager, final UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void onSuccessfulAuthentication(final HttpServletRequest request,
                                              final HttpServletResponse response,
                                              final Authentication authentication) throws IOException {
        super.onSuccessfulAuthentication(request, response, authentication);
        Optional.ofNullable(authentication.getPrincipal())
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast)
                .map(UserDetailsImpl::getId)
                .ifPresent(getUserService()::updateUserLastAccess);
    }

    protected UserService getUserService() {
        return userService;
    }
}
