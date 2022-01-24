package com.amtinez.api.rest.users.validations;

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
public class UniqueUserEmailValidatorUnitTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private UserService userService;

    @InjectMocks
    private UniqueUserEmailValidator uniqueUserEmailValidator;

    @Test
    public void testIsValid() {
        when(userService.existsUserEmail(anyString())).thenReturn(Boolean.FALSE);
        assertTrue(uniqueUserEmailValidator.isValid(StringUtils.EMPTY, constraintValidatorContext));
    }

    @Test
    public void testIsNotValid() {
        when(userService.existsUserEmail(anyString())).thenReturn(Boolean.TRUE);
        assertFalse(uniqueUserEmailValidator.isValid(StringUtils.EMPTY, constraintValidatorContext));
    }

}
