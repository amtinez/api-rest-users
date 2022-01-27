package com.amtinez.api.rest.users.factories;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MockUserSecurityContextFactory is the class in charge of creating a security context in order to create a mock of the logged in user for
 * the different tests.
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class MockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    private static final String MOCK_USER_FIRST_NAME = "mockFirstName";
    private static final String MOCK_USER_LAST_NAME = "mockLastName";

    @Override
    public SecurityContext createSecurityContext(final WithMockUser withMockUser) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        final UserDetailsImpl userDetails = UserDetailsImpl.builder()
                                                           .firstName(MOCK_USER_FIRST_NAME)
                                                           .lastName(MOCK_USER_LAST_NAME)
                                                           .authorities(generateAuthoritiesList(withMockUser))
                                                           .build();
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                                                                                      userDetails.getPassword(),
                                                                                      userDetails.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }

    private List<GrantedAuthority> generateAuthoritiesList(final WithMockUser withMockUser) {
        return Arrays.stream(withMockUser.authorities())
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }

}
