package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.facades.impl.UserFacadeImpl;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.RoleService;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.DISABLE_USER_BY_DEFAULT;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ENABLED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ENCRYPT_PASSWORD;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.LOCKED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.PASSWORD_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ROLES_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ROLE_MODEL_TO_ROLE;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ROLE_TO_ROLE_MODEL;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.ROLE_USER_BY_DEFAULT;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.UNLOCK_USER_BY_DEFAULT;
import static com.amtinez.api.rest.users.enums.Role.USER;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING_COMPONENT_MODEL)
public interface UserMapper {

    Logger LOG = LoggerFactory.getLogger(UserFacadeImpl.class);

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

    @Mapping(target = PASSWORD_PROPERTY, source = PASSWORD_PROPERTY, qualifiedByName = ENCRYPT_PASSWORD)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserModel updateUserModelFromUser(@MappingTarget final UserModel userModel,
                                      final User user,
                                      @Context final PasswordEncoder passwordEncoder);

    @Mapping(target = ENABLED_PROPERTY, expression = DISABLE_USER_BY_DEFAULT)
    @Mapping(target = LOCKED_PROPERTY, expression = UNLOCK_USER_BY_DEFAULT)
    @Mapping(target = PASSWORD_PROPERTY, source = PASSWORD_PROPERTY, qualifiedByName = ENCRYPT_PASSWORD)
    @Mapping(target = ROLES_PROPERTY, expression = ROLE_USER_BY_DEFAULT)
    UserModel userToUserModelRegisterStep(final User user,
                                          @Context final PasswordEncoder passwordEncoder,
                                          @Context final RoleService roleService);

    @Named(ENCRYPT_PASSWORD)
    static String encryptPassword(final String password, @Context final PasswordEncoder passwordEncoder) {
        return passwordEncoder != null ? passwordEncoder.encode(password) : password;
    }

    default Set<RoleModel> defaultRole(@Context final RoleService roleService) {
        if (roleService != null) {
            return roleService.findRole(USER.name())
                              .map(Set::of)
                              .orElseGet(() -> {
                                  LOG.error("The default role with name {} not exists", USER.name());
                                  return Set.of(roleService.saveRole(RoleModel.builder()
                                                                              .name(USER.name())
                                                                              .build()));
                              });
        }
        LOG.error("The role service is not injected");
        return Collections.emptySet();
    }

}
