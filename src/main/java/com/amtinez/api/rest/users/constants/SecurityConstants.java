package com.amtinez.api.rest.users.constants;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class SecurityConstants {

    public static final int BCRYPT_PASSWORD_ENCODER_STRENGTH = 10;

    public static final int PASSWORD_LIFETIME_MONTHS = 6;
    public static final int INACTIVE_LIFETIME_MONTHS = 3;

    public static final String HAS_ONLY_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String HAS_ANY_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')";
    public static final String PERMIT_ALL = "permitAll()";

    private SecurityConstants() {
    }

}
