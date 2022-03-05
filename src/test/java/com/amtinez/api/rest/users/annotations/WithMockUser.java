package com.amtinez.api.rest.users.annotations;

import com.amtinez.api.rest.users.factories.MockUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * WithMockUser is the interface used to implement a mock of the logged-in user.
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory.class)
public @interface WithMockUser {

    String username() default "mockFirstName@mockLastName.com";

    String name() default "mockFirstName mockLastName";

    String[] authorities() default {"ROLE_USER"};

}
