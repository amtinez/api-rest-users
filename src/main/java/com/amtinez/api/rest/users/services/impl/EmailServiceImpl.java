package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.configurations.properties.MailProperties;
import com.amtinez.api.rest.users.services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private MailProperties mailProperties;
    @Resource
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(final String address, final String subject, final String message) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(mailProperties.getFrom());
        email.setTo(address);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}
