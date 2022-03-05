package com.amtinez.api.rest.users.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ConfigurationProperties(prefix = "aru.mail")
public class MailProperties {

    private static final String FROM = "aru@noreply.com";

    public String getFrom() {
        return FROM;
    }

}
