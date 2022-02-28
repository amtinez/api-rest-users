package com.amtinez.api.rest.users.mappers;

import com.amtinez.api.rest.users.dtos.Token;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.UserModel;
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
 * Unit test for {@link PasswordResetTokenMapper}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PasswordResetTokenMapperUnitTest {

    private static final Long TEST_TOKEN_ID = 1L;
    private static final String TEST_TOKEN_CODE = "testTokenCode";

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private PasswordResetTokenMapperImpl mapper;

    private User user;
    private UserModel userModel;
    private PasswordResetTokenModel passwordResetTokenModel;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                   .build();
        userModel = UserModel.builder()
                             .build();
        passwordResetTokenModel = PasswordResetTokenModel.builder()
                                                         .id(TEST_TOKEN_ID)
                                                         .code(TEST_TOKEN_CODE)
                                                         .user(userModel)
                                                         .build();
        Mockito.when(userMapper.userToUserModel(user)).thenReturn(userModel);
        Mockito.when(userMapper.userModelToUser(userModel)).thenReturn(user);
    }

    @Test
    void tokenModelToToken() {
        final Token token = mapper.tokenModelToToken(passwordResetTokenModel);
        assertThat(token.getId()).isEqualTo(TEST_TOKEN_ID);
        assertThat(token.getCode()).isEqualTo(TEST_TOKEN_CODE);
        assertThat(token.getUser()).isEqualTo(user);
    }

    @Test
    void nullTokenModelToToken() {
        assertNull(mapper.tokenModelToToken(null));
    }

    @Test
    void userToTokenModel() {
        final PasswordResetTokenModel passwordResetToken = mapper.userToTokenModel(user);
        assertThat(passwordResetToken.getUser()).isEqualTo(userModel);
        assertThat(passwordResetToken.getCode()).isNotBlank();
        assertThat(passwordResetToken.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(passwordResetToken.getExpiryDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    void nullUserToTokenModel() {
        assertNull(mapper.userToTokenModel(null));
    }

}
