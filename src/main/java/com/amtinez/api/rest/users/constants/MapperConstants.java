package com.amtinez.api.rest.users.constants;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class MapperConstants {

    public static final String SPRING_COMPONENT_MODEL = "spring";

    public static final class User {

        public static final String PASSWORD_PROPERTY = "password";
        public static final String ENABLED_PROPERTY = "enabled";

        public static final String ENCRYPT_PASSWORD = "encryptPassword";
        public static final String ROLE_MODEL_TO_ROLE = "roleModelToRole";
        public static final String ROLE_TO_ROLE_MODEL = "roleToRoleModel";

        public static final String DISABLE_USER_BY_DEFAULT = "java(Boolean.FALSE)";

        private User() {
        }
    }

    private MapperConstants() {
    }

}
