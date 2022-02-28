package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.User;
import org.mapstruct.Mapping;

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
public interface TokenMapper<T, K> {

    T tokenModelToToken(final K tokenModel);

    @Mapping(target = ID_PROPERTY, ignore = true)
    @Mapping(target = CODE_PROPERTY, expression = GENERATE_UUID)
    @Mapping(target = CREATION_DATE_PROPERTY, expression = CREATION_DATE)
    @Mapping(target = EXPIRY_DATE_PROPERTY, expression = EXPIRY_DATE)
    @Mapping(target = USER_PROPERTY, source = USER_OBJECT)
    K userToTokenModel(final User user);

}
