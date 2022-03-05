package com.amtinez.api.rest.users.configurations;

import com.amtinez.api.rest.users.configurations.properties.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class GlobalConfiguration {

}
