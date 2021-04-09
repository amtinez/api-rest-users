package com.amtinez.api.rest.users.utils;

import com.amtinez.api.rest.users.models.RoleModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class RoleUtils {

    private RoleUtils() {
    }

    /**
     * Returns the name of the role prefixed with "ROLE_"
     *
     * @param roleModel the role
     * @return the prefixed role name
     */
    public static String getPrefixedName(final RoleModel roleModel) {
        return String.format("ROLE_%s", StringUtils.toRootUpperCase(roleModel.getName()));
    }

}
