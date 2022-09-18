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
public abstract class AbstractMailIntegrationTest {

    @Resource
    private GreenMail greenMail;

    @BeforeAll
    protected void startSmtp() {
        getGreenMail().start();
    }

    @AfterAll
    protected void stopSmtp() {
        getGreenMail().stop();
    }

    protected GreenMail getGreenMail() {
        return greenMail;
    }

}
