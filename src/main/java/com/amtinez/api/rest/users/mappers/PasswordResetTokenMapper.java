package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.UUID;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = SPRING_COMPONENT_MODEL,
        uses = UserMapper.class,
        imports = {LocalDate.class, UUID.class})
public interface PasswordResetTokenMapper extends TokenMapper<PasswordResetToken, PasswordResetTokenModel> {

}
