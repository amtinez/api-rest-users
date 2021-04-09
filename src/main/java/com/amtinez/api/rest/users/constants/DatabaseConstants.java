package com.amtinez.api.rest.users.constants;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class DatabaseConstants {

    public static final class Table {

        public static final class User {

            public static final String TABLE_NAME = "users";
            public static final String FIRST_NAME_FIELD = "first_name";
            public static final int FIRST_NAME_FIELD_LENGTH = 50;
            public static final String LAST_NAME_FIELD = "last_name";
            public static final int LAST_NAME_FIELD_LENGTH = 50;
            public static final String EMAIL_FIELD = "email";
            public static final int EMAIL_FIELD_LENGTH = 100;
            public static final String PASSWORD_FIELD = "password";
            public static final int PASSWORD_FIELD_LENGTH = 100;
            public static final String BIRTHDAY_DATE_FIELD = "birthday_date";
            public static final String LAST_ACCESS_DATE_FIELD = "last_access_date";
            public static final String LAST_PASSWORD_UPDATE_DATE_FIELD = "last_password_update_date";
            public static final String ENABLED_FIELD = "enabled";
            public static final String LOCKED_FIELD = "locked";

            private User() {
            }
        }

        public static final class Role {

            public static final String TABLE_NAME = "roles";
            public static final String NAME_FIELD = "name";
            public static final int NAME_FIELD_LENGTH = 50;

            private Role() {
            }
        }

        public static final class UsersRoles {

            public static final String TABLE_NAME = "users_roles";
            public static final String USER_ID_FIELD = "user_id";
            public static final String ROLE_ID_FIELD = "role_id";

            private UsersRoles() {
            }
        }

        public static final class Token {

            public static final String CODE_FIELD = "code";
            public static final int CODE_FIELD_LENGTH = 50;
            public static final String CREATION_DATE_FIELD = "creation_date";
            public static final String EXPIRY_DATE_FIELD = "expiry_date";
            public static final String USER_ID_FIELD = "user_id";

            private Token() {
            }
        }

        public static final class PasswordResetToken {

            public static final String TABLE_NAME = "password_reset_tokens";

            private PasswordResetToken() {
            }
        }

        public static final class UserVerificationToken {

            public static final String TABLE_NAME = "user_verification_tokens";

            private UserVerificationToken() {
            }
        }

        private Table() {
        }
    }

    private DatabaseConstants() {
    }

}
