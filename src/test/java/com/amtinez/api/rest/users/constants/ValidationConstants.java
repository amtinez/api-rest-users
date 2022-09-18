package com.amtinez.api.rest.users.constants;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class ValidationConstants {

    public static final class Fields {

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String PASSWORDS = "passwords";
        public static final String BIRTHDAY_DATE = "birthdayDate";

        public static final class Values {

            public static final String ONE_LETTER = "a";
            public static final String OVER_SIZED_EMAIL = ONE_LETTER.repeat(50)
                                                                    .concat("@")
                                                                    .concat(ONE_LETTER.repeat(50))
                                                                    .concat(".com");

            private Values() {
            }
        }

        private Fields() {
        }
    }

    public static final class Messages {

        public static final String NULL = "must not be null";
        public static final String BLANK = "must not be blank";
        public static final String OVERSIZE_50 = "size must be between 0 and 50";
        public static final String OVERSIZE_100 = "size must be between 0 and 100";
        public static final String WELL_FORMED_EMAIL = "must be a well-formed email address";
        public static final String EXISTS_EMAIL = "an account is already registered with this email address";
        public static final String EXISTS_ROLE = "a role with that name already exists";
        public static final String NOT_EQUALS_PASSWORDS = "passwords are not equal";

        private Messages() {
        }
    }

    private ValidationConstants() {
    }

}
