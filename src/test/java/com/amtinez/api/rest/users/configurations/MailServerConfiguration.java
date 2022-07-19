package com.amtinez.api.rest.users.configurations;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Configuration
public class MailServerConfiguration {

    private static int port;
    private static String bindAddress;
    private static String protocol;

    @Bean
    public GreenMail getGreenMail() {
        return new GreenMail(new ServerSetup(port, bindAddress, protocol));
    }

    @Value("${spring.mail.port}")
    private void setPort(final int port) {
        MailServerConfiguration.port = port;
    }

    @Value("${spring.mail.host}")
    private void setBindAddress(final String bindAddress) {
        MailServerConfiguration.bindAddress = bindAddress;
    }

    @Value("${spring.mail.protocol}")
    private void setProtocol(final String protocol) {
        MailServerConfiguration.protocol = protocol;
    }
}
