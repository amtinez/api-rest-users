package com.amtinez.api.rest.users.common;

import com.icegreen.greenmail.util.GreenMail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseMailIntegrationTest {

    @Resource
    private GreenMail greenMail;

    @BeforeAll
    public void startSmtp() {
        getGreenMail().start();
    }

    @AfterAll
    public void stopSmtp() {
        getGreenMail().stop();
    }

    public GreenMail getGreenMail() {
        return greenMail;
    }

}
