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
        public static final String LAST_ACCESS_DATE_PROPERTY = "lastAccessDate";
        public static final String LAST_PASSWORD_UPDATE_DATE_PROPERTY = "lastPasswordUpdateDate";
        public static final String ROLES_PROPERTY = "roles";

        public static final String ENCRYPT_PASSWORD = "encryptPassword";
        public static final String ROLE_MODEL_TO_ROLE = "roleModelToRole";
        public static final String ROLE_TO_ROLE_MODEL = "roleToRoleModel";

        public static final String DISABLE_USER_BY_DEFAULT = "java(Boolean.FALSE)";
        public static final String UNLOCK_USER_BY_DEFAULT = "java(Boolean.FALSE)";
        public static final String LAST_ACCESS_DATE_BY_DEFAULT = "java(LocalDateTime.now())";
        public static final String LAST_PASSWORD_UPDATE_DATE_BY_DEFAULT = "java(LocalDateTime.now())";
        public static final String ROLE_USER_BY_DEFAULT = "java(defaultRole(roleService))";

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

    public static final class Token {

        public static final String ID_PROPERTY = "id";
        public static final String CODE_PROPERTY = "code";
        public static final String CREATION_DATE_PROPERTY = "creationDate";
        public static final String EXPIRY_DATE_PROPERTY = "expiryDate";
        public static final String USER_PROPERTY = "user";
        public static final String USER_OBJECT = "user";

        public static final String GENERATE_UUID = "java(UUID.randomUUID().toString())";
        public static final String CREATION_DATE = "java(LocalDateTime.now())";
        public static final String EXPIRY_DATE = "java(LocalDateTime.now().plusDays(1))";

        private Token() {
        }
    }

    private MapperConstants() {
    }

}
