package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.dtos.UserVerificationToken;
import com.amtinez.api.rest.users.mappers.UserVerificationTokenMapper;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link UserVerificationTokenFacadeImpl}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserVerificationTokenFacadeUnitTest {

    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Mock
    private UserVerificationTokenMapper userVerificationTokenMapper;
    @Mock
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;

    @InjectMocks
    private UserVerificationTokenFacadeImpl userVerificationTokenFacade;

    private UserVerificationTokenModel userVerificationTokenModel;

    @BeforeEach
    public void setUp() {
        userVerificationTokenModel = UserVerificationTokenModel.builder()
                                                               .expiryDate(LocalDateTime.now().plusDays(1))
                                                               .build();
        Mockito.when(userVerificationTokenService.findToken(TEST_TOKEN_CODE)).thenReturn(Optional.of(userVerificationTokenModel));
        Mockito.when(userVerificationTokenMapper.tokenModelToToken(userVerificationTokenModel)).thenReturn(UserVerificationToken.builder()
                                                                                                                                .build());
    }

    @Test
    void testGetUnexpiredToken() {
        assertTrue(userVerificationTokenFacade.getUnexpiredToken(TEST_TOKEN_CODE).isPresent());
    }

    @Test
    void testGetUnexpiredTokenExpired() {
        userVerificationTokenModel.setExpiryDate(LocalDateTime.now().minusDays(1));
        assertTrue(userVerificationTokenFacade.getUnexpiredToken(TEST_TOKEN_CODE).isEmpty());
    }

}
