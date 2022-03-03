package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.UserVerificationToken;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = SPRING_COMPONENT_MODEL,
        uses = UserMapper.class,
        imports = {LocalDateTime.class, UUID.class})
public interface UserVerificationTokenMapper extends TokenMapper<UserVerificationToken, UserVerificationTokenModel> {

}
