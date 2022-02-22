package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.dtos.Token;
import com.amtinez.api.rest.users.facades.TokenFacade;
import com.amtinez.api.rest.users.mappers.UserVerificationTokenMapper;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.services.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class UserVerificationTokenFacadeImpl implements TokenFacade<UserVerificationTokenModel> {

    @Resource
    private UserVerificationTokenMapper userVerificationTokenMapper;

    @Resource
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;

    @Override
    public Optional<Token> getUnexpiredToken(final String code) {
        return userVerificationTokenService.findToken(code)
                                           .filter(token -> this.isUnexpiredToken(userVerificationTokenService, token))
                                           .map(userVerificationTokenMapper::tokenModelToToken);
    }

}
