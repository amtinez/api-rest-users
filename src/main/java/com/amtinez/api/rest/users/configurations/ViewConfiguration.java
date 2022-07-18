package com.amtinez.api.rest.users.configurations;

import com.amtinez.api.rest.users.enums.Role;
import com.amtinez.api.rest.users.views.View;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Configuration
public class ViewConfiguration {

    @Bean
    public Map<Role, Class<?>> getRoleViewMap() {
        return new EnumMap<>(Map.of(Role.USER, View.User.class,
                                    Role.ADMIN, View.Admin.class));
    }

}
