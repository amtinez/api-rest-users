package com.amtinez.api.rest.users.constants;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class ValidationConstants {

    public static final class User {

        public static final int FIRST_NAME_MAX_FIELD_LENGTH = 50;
        public static final int LAST_NAME_MAX_FIELD_LENGTH = 50;
        public static final int EMAIL_MAX_FIELD_LENGTH = 100;
        public static final int PASSWORD_MAX_FIELD_LENGTH = 100;

        private User() {
        }
    }

    public static final class Role {

        public static final int NAME_MAX_FIELD_LENGTH = 50;

        private Role() {
        }
    }

    private ValidationConstants() {
    }

}
