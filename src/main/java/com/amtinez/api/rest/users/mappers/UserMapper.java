package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;
import static com.amtinez.api.rest.users.constants.MapperConstants.User.PASSWORD_PROPERTY;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING_COMPONENT_MODEL)
public interface UserMapper {

    @Mapping(target = PASSWORD_PROPERTY, ignore = true)
    User userModelToUser(final UserModel userModel);

    UserModel userToUserModel(final User user);

}
