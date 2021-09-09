package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.models.RoleModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING_COMPONENT_MODEL)
public interface RoleMapper {

    Role roleModelToRole(final RoleModel roleModel);

    RoleModel roleToRoleModel(final Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoleModel updateRoleModelFromRole(@MappingTarget final RoleModel roleModel, final Role role);

}
