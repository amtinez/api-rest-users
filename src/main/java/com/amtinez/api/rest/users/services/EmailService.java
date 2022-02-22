package com.amtinez.api.rest.users.services;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public interface EmailService {

    /**
     * Send an e-mail
     *
     * @param address the address
     * @param subject the subject
     * @param message the message
     */
    void sendEmail(final String address, final String subject, final String message);

}
