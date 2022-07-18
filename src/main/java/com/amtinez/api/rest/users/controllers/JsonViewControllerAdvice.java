package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.enums.Role;
import com.amtinez.api.rest.users.utils.RoleUtils;
import com.amtinez.api.rest.users.views.View;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ControllerAdvice
public class JsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Resource
    private Map<Role, Class<?>> roleViewMap;

    @Override
    protected void beforeBodyWriteInternal(final MappingJacksonValue bodyContainer,
                                           final MediaType contentType,
                                           final MethodParameter returnType,
                                           final ServerHttpRequest request,
                                           final ServerHttpResponse response) {
        Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .map(RoleUtils::getNameWithoutPrefix)
                .map(Role::valueOf)
                .map(roleViewMap::get)
                .filter(Objects::nonNull)
                .findAny()
                .ifPresentOrElse(bodyContainer::setSerializationView,
                                 () -> bodyContainer.setSerializationView(View.Anonymous.class));
    }

}
