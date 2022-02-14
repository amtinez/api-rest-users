package com.amtinez.api.rest.users.mappers;

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
 * Unit test for {@link UserVerificationTokenModelMapper}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserVerificationTokenModelMapperUnitTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserVerificationTokenModelMapperImpl mapper;

    private User user;
    private UserModel userModel;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                   .build();
        userModel = UserModel.builder()
                             .build();
        Mockito.when(userMapper.userToUserModel(user)).thenReturn(userModel);
    }

    @Test
    public void userToUserVerificationTokenModel() {
        final UserVerificationTokenModel userVerificationToken = mapper.userToUserVerificationTokenModel(user);
        assertThat(userVerificationToken.getUser()).isEqualTo(userModel);
        assertThat(userVerificationToken.getCode()).isNotBlank();
        assertThat(userVerificationToken.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(userVerificationToken.getExpiryDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    public void nullUserToUserVerificationTokenModel() {
        assertNull(mapper.userToUserVerificationTokenModel(null));
    }

}
