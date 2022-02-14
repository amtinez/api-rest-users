package com.amtinez.api.rest.users.events;

import com.amtinez.api.rest.users.dtos.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public class RegistrationSuccessEvent extends ApplicationEvent {

    public RegistrationSuccessEvent(final User user) {
        super(user);
    }

}
