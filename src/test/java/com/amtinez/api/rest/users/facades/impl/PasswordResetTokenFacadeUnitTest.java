package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.mappers.PasswordResetTokenMapper;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link PasswordResetTokenFacadeImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PasswordResetTokenFacadeUnitTest {

    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Mock
    private PasswordResetTokenMapper passwordResetTokenMapper;
    @Mock
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;

    @InjectMocks
    private PasswordResetTokenFacadeImpl passwordResetTokenFacade;

    private PasswordResetTokenModel passwordResetTokenModel;

    @BeforeEach
    public void setUp() {
        passwordResetTokenModel = PasswordResetTokenModel.builder()
                                                         .expiryDate(LocalDate.now().plusDays(1))
                                                         .build();
        Mockito.when(passwordResetTokenService.findToken(TEST_TOKEN_CODE)).thenReturn(Optional.of(passwordResetTokenModel));
        Mockito.when(passwordResetTokenMapper.tokenModelToToken(passwordResetTokenModel)).thenReturn(PasswordResetToken.builder()
                                                                                                                       .build());
    }

    @Test
    void testGetUnexpiredToken() {
        assertTrue(passwordResetTokenFacade.getUnexpiredToken(TEST_TOKEN_CODE).isPresent());
    }

    @Test
    void testGetUnexpiredTokenExpired() {
        passwordResetTokenModel.setExpiryDate(LocalDate.now().minusDays(1));
        assertTrue(passwordResetTokenFacade.getUnexpiredToken(TEST_TOKEN_CODE).isEmpty());
    }

}
