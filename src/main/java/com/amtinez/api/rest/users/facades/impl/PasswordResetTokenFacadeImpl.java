package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.facades.TokenFacade;
import com.amtinez.api.rest.users.mappers.PasswordResetTokenMapper;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.services.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class PasswordResetTokenFacadeImpl implements TokenFacade<PasswordResetTokenModel, PasswordResetToken> {

    @Resource
    private PasswordResetTokenMapper passwordResetTokenMapper;

    @Resource
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;

    @Override
    public Optional<PasswordResetToken> getUnexpiredToken(final String code) {
        return passwordResetTokenService.findToken(code)
                                        .filter(token -> this.isUnexpiredToken(passwordResetTokenService, token))
                                        .map(passwordResetTokenMapper::tokenModelToToken);
    }

}
