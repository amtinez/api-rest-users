package com.amtinez.api.rest.users.annotations;

import com.amtinez.api.rest.users.factories.MockAdminUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * WithMockAdminUser is the interface used to implement a mock of the logged-in admin user.
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockAdminUserSecurityContextFactory.class)
public @interface WithMockAdminUser {

    String username() default "mockFirstName@mockFirstName.com";

    String name() default "mockFirstName mockLastName";

}
