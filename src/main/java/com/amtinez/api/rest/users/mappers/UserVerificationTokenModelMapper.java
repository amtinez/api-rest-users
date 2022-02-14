package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.UUID;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.CODE_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.CREATION_DATE;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.CREATION_DATE_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.EXPIRY_DATE;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.EXPIRY_DATE_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.GENERATE_UUID;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.ID_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.USER_OBJECT;
import static com.amtinez.api.rest.users.constants.MapperConstants.Token.USER_PROPERTY;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = SPRING_COMPONENT_MODEL,
        uses = UserMapper.class,
        imports = {LocalDate.class, UUID.class})
public interface UserVerificationTokenModelMapper {

    @Mapping(target = ID_PROPERTY, ignore = true)
    @Mapping(target = CODE_PROPERTY, expression = GENERATE_UUID)
    @Mapping(target = CREATION_DATE_PROPERTY, expression = CREATION_DATE)
    @Mapping(target = EXPIRY_DATE_PROPERTY, expression = EXPIRY_DATE)
    @Mapping(target = USER_PROPERTY, source = USER_OBJECT)
    UserVerificationTokenModel userToUserVerificationTokenModel(final User user);

}
