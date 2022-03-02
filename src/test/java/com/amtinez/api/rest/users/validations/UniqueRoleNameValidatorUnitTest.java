package com.amtinez.api.rest.users.validations;

import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.services.RoleService;
import com.amtinez.api.rest.users.validations.validators.UniqueRoleNameValidator;
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
 * Unit test for {@link UniqueRoleNameValidator}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UniqueRoleNameValidatorUnitTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private UniqueRoleNameValidator uniqueRoleNameValidator;

    @Test
    void testIsValid() {
        when(roleService.findRole(anyString())).thenReturn(Optional.empty());
        assertTrue(uniqueRoleNameValidator.isValid(StringUtils.EMPTY, constraintValidatorContext));
    }

    @Test
    void testIsNotValid() {
        when(roleService.findRole(anyString())).thenReturn(Optional.of(RoleModel.builder()
                                                                                .build()));
        assertFalse(uniqueRoleNameValidator.isValid(StringUtils.EMPTY, constraintValidatorContext));
    }

}
