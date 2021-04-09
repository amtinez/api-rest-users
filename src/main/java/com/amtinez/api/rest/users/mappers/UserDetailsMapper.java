package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.utils.RoleUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.amtinez.api.rest.users.constants.MapperConstants.SPRING_COMPONENT_MODEL;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.ACCOUNT_NON_EXPIRED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.ACCOUNT_NON_LOCKED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.AUTHORITIES_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.CREDENTIALS_NON_EXPIRED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.IS_ACCOUNT_NON_EXPIRED;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.IS_ACCOUNT_NON_LOCKED;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.IS_CREDENTIALS_NON_EXPIRED;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.LAST_ACCESS_DATE_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.LAST_PASSWORD_UPDATE_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.LOCKED_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.ROLES_PROPERTY;
import static com.amtinez.api.rest.users.constants.MapperConstants.UserDetails.ROLES_TO_AUTHORITIES;
import static com.amtinez.api.rest.users.constants.SecurityConstants.INACTIVE_LIFETIME_MONTHS;
import static com.amtinez.api.rest.users.constants.SecurityConstants.PASSWORD_LIFETIME_MONTHS;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING_COMPONENT_MODEL)
public interface UserDetailsMapper {

    @Mapping(target = AUTHORITIES_PROPERTY, source = ROLES_PROPERTY, qualifiedByName = ROLES_TO_AUTHORITIES)
    @Mapping(target = ACCOUNT_NON_LOCKED_PROPERTY, source = LOCKED_PROPERTY, qualifiedByName = IS_ACCOUNT_NON_LOCKED)
    @Mapping(target = ACCOUNT_NON_EXPIRED_PROPERTY, source = LAST_ACCESS_DATE_PROPERTY, qualifiedByName = IS_ACCOUNT_NON_EXPIRED)
    @Mapping(target = CREDENTIALS_NON_EXPIRED_PROPERTY,
             source = LAST_PASSWORD_UPDATE_PROPERTY,
             qualifiedByName = IS_CREDENTIALS_NON_EXPIRED)
    UserDetailsImpl userModelToUserDetails(final UserModel userModel);

    @Named(ROLES_TO_AUTHORITIES)
    static Collection<GrantedAuthority> rolesToAuthorities(final Set<RoleModel> roles) {
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(RoleUtils.getPrefixedName(role)))
                    .collect(Collectors.toList());
    }

    @Named(IS_ACCOUNT_NON_LOCKED)
    static boolean isAccountNonLocked(final Boolean locked) {
        return BooleanUtils.isFalse(locked);
    }

    @Named(IS_ACCOUNT_NON_EXPIRED)
    static boolean isAccountNonExpired(final LocalDate lastAccessDate) {
        return lastAccessDate != null && ChronoUnit.MONTHS.between(lastAccessDate, LocalDate.now()) < INACTIVE_LIFETIME_MONTHS;
    }

    @Named(IS_CREDENTIALS_NON_EXPIRED)
    static boolean isCredentialsNonExpired(final LocalDate lastPasswordUpdateDate) {
        return lastPasswordUpdateDate != null
            && ChronoUnit.MONTHS.between(lastPasswordUpdateDate, LocalDate.now()) < PASSWORD_LIFETIME_MONTHS;
    }

}
