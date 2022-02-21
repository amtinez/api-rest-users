package com.amtinez.api.rest.users.facades;

import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
public class TokenFacadeUnitTest {

    @Mock
    private TokenService<UserVerificationTokenModel> tokenService;

    @Spy
    private TokenFacade<UserVerificationTokenModel> tokenFacade;

    private UserVerificationTokenModel tokenModel;

    @BeforeEach
    public void setUp() {
        tokenModel = UserVerificationTokenModel.builder()
                                               .expiryDate(LocalDate.now().plusDays(1))
                                               .build();
    }

    @Test
    public void testIsUnexpiredToken() {
        assertTrue(tokenFacade.isUnexpiredToken(tokenService, tokenModel));
    }

    @Test
    public void testIsUnexpiredTokenExpired() {
        tokenModel.setExpiryDate(LocalDate.now().minusDays(1));
        assertFalse(tokenFacade.isUnexpiredToken(tokenService, tokenModel));
        Mockito.verify(tokenService).deleteToken(tokenModel);
    }

}
