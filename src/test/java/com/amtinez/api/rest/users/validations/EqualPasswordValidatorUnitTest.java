package com.amtinez.api.rest.users.validations;

import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.validations.validators.EqualPasswordValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link EqualPasswordValidator}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EqualPasswordValidatorUnitTest {

    private static final String TEST_PASSWORD = "testPassword";

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private EqualPasswordValidator equalPasswordValidator;

    @Test
    void testIsValid() {
        assertTrue(equalPasswordValidator.isValid(PasswordResetToken.builder()
                                                                    .password(TEST_PASSWORD)
                                                                    .repeatedPassword(TEST_PASSWORD)
                                                                    .build(),
                                                  constraintValidatorContext));
    }

    @Test
    void testIsNotValid() {
        assertFalse(equalPasswordValidator.isValid(PasswordResetToken.builder()
                                                                     .password(TEST_PASSWORD)
                                                                     .repeatedPassword(StringUtils.EMPTY)
                                                                     .build(),
                                                   constraintValidatorContext));
    }

}
