package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.DISABLE_USER_BY_DEFAULT;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ENABLED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ENCRYPT_PASSWORD;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.PASSWORD_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ROLE_MODEL_TO_ROLE;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ROLE_TO_ROLE_MODEL;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING_COMPONENT_MODEL)
public interface UserMapper {

    @Mapping(target = PASSWORD_PROPERTY, ignore = true)
    User userModelToUser(final UserModel userModel);

    @IterableMapping(qualifiedByName = ROLE_MODEL_TO_ROLE)
    Set<Role> roleModelsToRoles(final Set<RoleModel> roleModels);

    @Named(ROLE_MODEL_TO_ROLE)
    Role roleModelToRole(final RoleModel roleModel);

    UserModel userToUserModel(final User user);

    @IterableMapping(qualifiedByName = ROLE_TO_ROLE_MODEL)
    Set<RoleModel> rolesToRoleModels(final Set<Role> roles);

    @Named(ROLE_TO_ROLE_MODEL)
    RoleModel roleToRoleModel(final Role role);

    @Mapping(target = ENABLED_PROPERTY, expression = DISABLE_USER_BY_DEFAULT)
    @Mapping(target = PASSWORD_PROPERTY, source = PASSWORD_PROPERTY, qualifiedByName = ENCRYPT_PASSWORD)
    UserModel userToUserModelRegisterStep(final User user, @Context final PasswordEncoder passwordEncoder);

    @Named(ENCRYPT_PASSWORD)
    static String encryptPassword(final String password, @Context final PasswordEncoder passwordEncoder) {
        return passwordEncoder != null ? passwordEncoder.encode(password) : password;
    }

}
