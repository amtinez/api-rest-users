package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.models.RoleModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class RoleUtils {

    private static final String PREFIX = "ROLE_";

    private RoleUtils() {
    }

    /**
     * Returns the name of the role prefixed with "ROLE_"
     *
     * @param roleModel the role
     * @return the prefixed role name
     */
    public static String getPrefixedName(final RoleModel roleModel) {
        return String.format("%s%s", PREFIX, StringUtils.toRootUpperCase(roleModel.getName()));
    }

    /**
     * Returns the name of the role without the "ROLE_" prefix
     *
     * @param roleName the role
     * @return the prefixed role name
     */
    public static String getNameWithoutPrefix(final String roleName) {
        return StringUtils.removeStart(roleName, PREFIX);
    }

}
