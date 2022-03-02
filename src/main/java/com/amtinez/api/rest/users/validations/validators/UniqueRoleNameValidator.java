package com.amtinez.api.rest.users.validations.validators;

import com.amtinez.api.rest.users.annotations.UniqueRoleName;
import com.amtinez.api.rest.users.services.RoleService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {

    @Resource
    private RoleService roleService;

    /**
     * Returns whether the role name exists or not
     *
     * @param name    the role name
     * @param context the constraint validator context
     * @return if the role name is valid
     */
    @Override
    public boolean isValid(final String name, final ConstraintValidatorContext context) {
        return roleService.findRole(name).isEmpty();
    }

}
