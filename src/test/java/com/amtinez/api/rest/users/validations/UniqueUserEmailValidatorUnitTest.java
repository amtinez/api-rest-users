package com.amtinez.api.rest.users.validations;

import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.UserService;
import com.amtinez.api.rest.users.validations.validators.UniqueUserEmailValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link UniqueUserEmailValidator}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UniqueUserEmailValidatorUnitTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private UserService userService;

    @InjectMocks
    private UniqueUserEmailValidator uniqueUserEmailValidator;

    @Test
    void testIsValid() {
        when(userService.findUser(anyString())).thenReturn(Optional.empty());
        assertTrue(uniqueUserEmailValidator.isValid(StringUtils.EMPTY, constraintValidatorContext));
    }

    @Test
    void testIsNotValid() {
        when(userService.findUser(anyString())).thenReturn(Optional.of(UserModel.builder()
                                                                                .build()));
        assertFalse(uniqueUserEmailValidator.isValid(StringUtils.EMPTY, constraintValidatorContext));
    }

}
