package com.amtinez.api.rest.users.constants;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class MapperConstants {

    public static final String SPRING_COMPONENT_MODEL = "spring";

    public static final class User {

        public static final String PASSWORD_PROPERTY = "password";
        public static final String ENABLED_PROPERTY = "enabled";
        public static final String LOCKED_PROPERTY = "locked";

        public static final String ENCRYPT_PASSWORD = "encryptPassword";
        public static final String ROLE_MODEL_TO_ROLE = "roleModelToRole";
        public static final String ROLE_TO_ROLE_MODEL = "roleToRoleModel";

        public static final String DISABLE_USER_BY_DEFAULT = "java(Boolean.FALSE)";
        public static final String UNLOCK_USER_BY_DEFAULT = "java(Boolean.FALSE)";

        private User() {
        }
    }

    public static final class UserDetails {

        public static final String AUTHORITIES_PROPERTY = "authorities";
        public static final String ACCOUNT_NON_LOCKED_PROPERTY = "accountNonLocked";
        public static final String ACCOUNT_NON_EXPIRED_PROPERTY = "accountNonExpired";
        public static final String CREDENTIALS_NON_EXPIRED_PROPERTY = "credentialsNonExpired";
        public static final String ROLES_PROPERTY = "roles";
        public static final String LAST_ACCESS_DATE_PROPERTY = "lastAccessDate";
        public static final String LOCKED_PROPERTY = "locked";
        public static final String LAST_PASSWORD_UPDATE_PROPERTY = "lastPasswordUpdateDate";

        public static final String ROLES_TO_AUTHORITIES = "rolesToAuthorities";
        public static final String IS_ACCOUNT_NON_LOCKED = "isAccountNonLocked";
        public static final String IS_ACCOUNT_NON_EXPIRED = "isAccountNonExpired";
        public static final String IS_CREDENTIALS_NON_EXPIRED = "isCredentialsNonExpired";

        private UserDetails() {
        }
    }

    private MapperConstants() {
    }

}
