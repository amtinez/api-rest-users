package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Token;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for {@link UserVerificationTokenMapper}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserVerificationTokenMapperUnitTest {

    private static final Long TEST_TOKEN_ID = 1L;
    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserVerificationTokenMapperImpl mapper;

    private User user;
    private UserModel userModel;
    private UserVerificationTokenModel userVerificationTokenModel;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                   .build();
        userModel = UserModel.builder()
                             .build();
        userVerificationTokenModel = UserVerificationTokenModel.builder()
                                                               .id(TEST_TOKEN_ID)
                                                               .code(TEST_TOKEN_CODE)
                                                               .user(userModel)
                                                               .build();
        Mockito.when(userMapper.userToUserModel(user)).thenReturn(userModel);
        Mockito.when(userMapper.userModelToUser(userModel)).thenReturn(user);
    }

    @Test
    public void tokenModelToToken() {
        final Token token = mapper.tokenModelToToken(userVerificationTokenModel);
        assertThat(token.getId()).isEqualTo(TEST_TOKEN_ID);
        assertThat(token.getCode()).isEqualTo(TEST_TOKEN_CODE);
        assertThat(token.getUser()).isEqualTo(user);
    }

    @Test
    public void nullTokenModelToToken() {
        assertNull(mapper.tokenModelToToken(null));
    }

    @Test
    public void userToTokenModel() {
        final UserVerificationTokenModel userVerificationToken = mapper.userToTokenModel(user);
        assertThat(userVerificationToken.getUser()).isEqualTo(userModel);
        assertThat(userVerificationToken.getCode()).isNotBlank();
        assertThat(userVerificationToken.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(userVerificationToken.getExpiryDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    public void nullUserToTokenModel() {
        assertNull(mapper.userToTokenModel(null));
    }

}
