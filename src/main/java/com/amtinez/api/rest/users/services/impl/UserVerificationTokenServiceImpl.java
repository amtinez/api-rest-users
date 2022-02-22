package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.daos.UserVerificationTokenDao;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.services.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class UserVerificationTokenServiceImpl implements TokenService<UserVerificationTokenModel> {

    @Resource
    private UserVerificationTokenDao userVerificationTokenDao;

    @Override
    public Optional<UserVerificationTokenModel> findToken(final Long id) {
        return userVerificationTokenDao.findById(id);
    }

    @Override
    public Optional<UserVerificationTokenModel> findToken(final String code) {
        return userVerificationTokenDao.findByCode(code);
    }

    @Override
    public Optional<UserVerificationTokenModel> findToken(final UserModel user) {
        return userVerificationTokenDao.findByUser(user);
    }

    @Override
    public UserVerificationTokenModel saveToken(final UserVerificationTokenModel token) {
        return userVerificationTokenDao.save(token);
    }

    @Override
    public void deleteToken(final UserVerificationTokenModel token) {
        userVerificationTokenDao.delete(token);
    }

    @Override
    public void deleteToken(final Long id) {
        userVerificationTokenDao.deleteById(id);
    }

    @Override
    public void deleteTokenByUserId(final Long id) {
        userVerificationTokenDao.deleteByUserId(id);
    }

}
