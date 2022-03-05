package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.amtinez.api.rest.users.constants.SecurityConstants.ROLE_ADMIN;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Check if the logged-in user is admin
     *
     * @return if the logged-in user is admin
     */
    public static boolean isAdminUser() {
        return SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .anyMatch(grantedAuthority -> grantedAuthority.equals(ROLE_ADMIN));
    }

    /**
     * Check if the user is the same as the logged-in user
     *
     * @param user the user
     * @return if the user is the same as the logged-in user
     */
    public static boolean isUserSameLoggedInUser(final User user) {
        return Optional.of(SecurityContextHolder.getContext().getAuthentication())
                       .map(Authentication::getPrincipal)
                       .filter(UserDetailsImpl.class::isInstance)
                       .map(UserDetailsImpl.class::cast)
                       .map(UserDetailsImpl::getId)
                       .filter(id -> id.equals(user.getId()))
                       .isPresent();
    }

    /**
     * Check if the user logged-in can update this user
     *
     * @param user the user to update
     * @return if the user logged-in can update this user
     */
    public static boolean canLoggedInUserUpdateThisUser(final User user) {
        return SecurityUtils.isAdminUser() ? Boolean.TRUE
                                           : SecurityUtils.isUserSameLoggedInUser(user);
    }

}
